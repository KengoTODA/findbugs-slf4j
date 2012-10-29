package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Repository.lookupClass;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

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
            } else if (seen == Constants.INVOKEINTERFACE || seen == Constants.INVOKEVIRTUAL) {
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
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("class not found", e);
        }
    }

    /**
     * @return true if given Item is throwable
     */
    boolean checkThrowable(Item stackItem) {
        return IS_THROWABLE.equals(stackItem.getUserValue());
    }

}
