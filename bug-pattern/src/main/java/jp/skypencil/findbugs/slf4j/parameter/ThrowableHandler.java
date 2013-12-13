package jp.skypencil.findbugs.slf4j.parameter;

import jp.skypencil.findbugs.slf4j.JavaType;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;

public class ThrowableHandler {
    private static final String IS_THROWABLE = "IS_THROWABLE";

    void sawOpcode(OpcodeStackDetector detector, int seen) {
        if (detector.atCatchBlock()) {
            // the head of stack should be a Throwable instance
            detector.getStack().getStackItem(0).setUserValue(IS_THROWABLE);
        }
    }

    /**
     * Call this method after #afterOpcode of detector.
     */
    void afterOpcode(OpcodeStackDetector detector, int seen) {
        OpcodeStack stack = detector.getStack();
        if (stack.isTop()) {
            // see https://github.com/eller86/findbugs-slf4j/issues/29
            System.err.printf("ThrowableHandler: stack is TOP, cannot be analyzed. %s:%d%n",
                    detector.getClassName(), detector.getPC());
            return;
        }
        if (seen == Constants.NEW && JavaType.from(detector.getClassConstantOperand()).isThrowable()) {
            Item createdThrowable = stack.getStackItem(0);
            createdThrowable.setUserValue(IS_THROWABLE);
        } else if (seen == Constants.INVOKEINTERFACE || seen == Constants.INVOKEVIRTUAL || seen == Constants.INVOKESPECIAL || seen == Constants.INVOKESTATIC) {
            String signature = detector.getMethodDescriptorOperand().getSignature();
            String returnType = signature.substring(1 + signature.lastIndexOf(')'));
            if (!returnType.startsWith("L")) {
                // primitive value should be non-throwable
                return;
            }

            returnType = returnType.substring(1, returnType.length() - 1).replaceAll("/", ".");
            if (JavaType.from(returnType).isThrowable()) {
                Item returnedThrowable = stack.getStackItem(0);
                returnedThrowable.setUserValue(IS_THROWABLE);
            }
        } else if (seen == Constants.ALOAD || seen == Constants.ALOAD_0 || seen == Constants.ALOAD_1 || seen == Constants.ALOAD_2 || seen == Constants.ALOAD_3) {
            JavaType typeOfLocalVar = loadLocalVar(detector);
            if (typeOfLocalVar != null && typeOfLocalVar.isThrowable()) {
                Item localThrowable = stack.getStackItem(0);
                localThrowable.setUserValue(IS_THROWABLE);
            }
        } else if (seen == Constants.GETFIELD || seen == Constants.GETSTATIC) {
            FieldDescriptor targetField = detector.getFieldDescriptorOperand();
            Type type = Type.getType(targetField.getSignature());
            if (type != null) {
                JavaType javaType = JavaType.from(type);
                if (javaType != null && javaType.isThrowable()) {
                    Item localThrowable = stack.getStackItem(0);
                    localThrowable.setUserValue(IS_THROWABLE);
                }
            }
        }
    }

    private JavaType loadLocalVar(OpcodeStackDetector detector) {
        int index = detector.getRegisterOperand();
        boolean isStaticMethod = detector.getMethodDescriptor().isStatic();
        if (!isStaticMethod) {
            if (index == 0) {
                // "this"
                return JavaType.from(detector.getThisClass());
            }
            index--;
        }

        Type[] arguments = detector.getMethod().getArgumentTypes();
        if (index >= arguments.length) {
            // we do not have to care about local variable, because other mechanism will mark it as exception
            return null;
        } else if (index < 0) {
            // report method dump to debug #18
            // see https://github.com/eller86/findbugs-slf4j/issues/18
            String dump = detector.getMethod().getCode().toString();
            int pc = detector.getPC();
            throw new IllegalArgumentException(String.format(
                    "Illegal index %d at PC %d. Please report this dump:%n%s",
                    index, pc, dump));
        } else {
            // method argument
            return JavaType.from(arguments[index]);
        }
    }

    /**
     * @return true if given Item is throwable
     */
    public boolean checkThrowable(Item stackItem) {
        return IS_THROWABLE.equals(stackItem.getUserValue());
    }

}
