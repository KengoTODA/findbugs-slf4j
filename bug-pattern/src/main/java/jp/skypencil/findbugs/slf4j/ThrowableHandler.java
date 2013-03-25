package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Repository.lookupClass;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;

class ThrowableHandler {
    private static final String IS_THROWABLE = "IS_THROWABLE";
    private final JavaClass throwable;

    ThrowableHandler() {
        try {
            this.throwable = lookupClass("java/lang/Throwable");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

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
        try {
            OpcodeStack stack = detector.getStack();
            if (seen == Constants.NEW && lookupClass(detector.getClassConstantOperand()).instanceOf(throwable)) {
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
                JavaClass returnClass = lookupClass(returnType);
                if (returnClass.instanceOf(throwable)) {
                    Item returnedThrowable = stack.getStackItem(0);
                    returnedThrowable.setUserValue(IS_THROWABLE);
                }
            } else if (seen == Constants.ALOAD || seen == Constants.ALOAD_0 || seen == Constants.ALOAD_1 || seen == Constants.ALOAD_2 || seen == Constants.ALOAD_3) {
                final int index;
                switch (seen) {
                case Constants.ALOAD_0: index = 0; break;
                case Constants.ALOAD_1: index = 1; break;
                case Constants.ALOAD_2: index = 2; break;
                case Constants.ALOAD_3: index = 3; break;
                default:
                    index = detector.getIntConstant();
                }
                JavaType typeOfLocalVar = loadLocalVar(detector, index);
                if (typeOfLocalVar != null) {
                    JavaClass javaClass = typeOfLocalVar.toJavaClass();
                    if (javaClass != null && javaClass.instanceOf(throwable)) {
                        Item localThrowable = stack.getStackItem(0);
                        localThrowable.setUserValue(IS_THROWABLE);
                    }
                }
            } else if (seen == Constants.GETFIELD || seen == Constants.GETSTATIC) {
                FieldDescriptor targetField = detector.getFieldDescriptorOperand();
                Type type = Type.getType(targetField.getSignature());
                if (type != null) {
                    JavaType javaType = JavaType.from(type);
                    if (javaType != null) {
                        JavaClass javaClass = javaType.toJavaClass();
                        if (javaClass != null && javaClass.instanceOf(throwable)) {
                            Item localThrowable = stack.getStackItem(0);
                            localThrowable.setUserValue(IS_THROWABLE);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("class not found", e);
        }
    }

    private JavaType loadLocalVar(OpcodeStackDetector detector, int index) {
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
        } else {
            // method argument
            return JavaType.from(arguments[index]);
        }
    }

    /**
     * @return true if given Item is throwable
     */
    boolean checkThrowable(Item stackItem) {
        return IS_THROWABLE.equals(stackItem.getUserValue());
    }

}
