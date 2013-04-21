package jp.skypencil.findbugs.slf4j;

import org.apache.bcel.Constants;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;

/**
 * This class is responsible to create/update ArrayData instance in userValue.
 */
class ArrayDataHandler {
    private final ThrowableHandler throwableHandler;

	ArrayDataHandler (ThrowableHandler throwableHandler) {
        this.throwableHandler = throwableHandler;
    }

    void sawOpcode(OpcodeStack stack, int seen) {
        if (seen == Constants.AASTORE) {
            checkStoredInstance(stack);
        }
    }

    /**
     * Call this method before #afterOpcode of detector.
     * If this method returns new userValue, attach it to the head value of stack
     * after #afterOpcode of detector is called.
     *
     * @return non-null if seen == ANEWARRAY
     */
    ArrayData afterOpcode(OpcodeStack stack, int seen) {
        if (seen != Constants.ANEWARRAY) {
            return null;
        }
        return tryToDetectArraySize(stack);
    }

    private void checkStoredInstance(OpcodeStack stack) {
        Item storedValue = stack.getStackItem(0);
        Item arrayIndexItem = stack.getStackItem(1);
        Item targetArray = stack.getStackItem(2);
        Object arrayIndex = arrayIndexItem.getConstant();

        if (arrayIndex instanceof Number) {
            ArrayData data = (ArrayData) targetArray.getUserValue();
            Number index = (Number) arrayIndex;
            if (data != null && data.getSize() - 1 == index.intValue()) {
                data.setThrowableAtLast(throwableHandler.checkThrowable(storedValue));
            }
        }
    }

    private ArrayData tryToDetectArraySize(OpcodeStack stack) {
        Item arraySizeItem = stack.getStackItem(0);
        final int arraySize;

        if (arraySizeItem != null && arraySizeItem.getConstant() instanceof Number) {
            // save array size as "user value"
            arraySize = ((Number) arraySizeItem.getConstant()).intValue();
        } else {
            // currently we ignore array which gets variable as array size (like `new Object[a]`)
            arraySize = -1;
        }

        return new ArrayData(arraySize);
    }
}
