package jp.skypencil.findbugs.slf4j.parameter;

import javax.annotation.OverridingMethodsMustInvokeSuper;


import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * <p>Basic detector to support detection about parameter array.
 * @author Kengo TODA
 */
public abstract class AbstractDetectorForParameterArray extends OpcodeStackDetector {
    private final ThrowableHandler throwableHandler;
    private final ArrayDataHandler arrayDataHandler;

    public AbstractDetectorForParameterArray() {
        throwableHandler = new ThrowableHandler();
        arrayDataHandler = new ArrayDataHandler(throwableHandler);
    }

    @Override
    public final void sawOpcode(int seen) {
        throwableHandler.sawOpcode(this, seen);
        try {
            sawOpcode(seen, throwableHandler);
        } finally {
            arrayDataHandler.sawOpcode(stack, seen);
        }
    }

    protected abstract void sawOpcode(int seen, ThrowableHandler throwableHandler);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void afterOpcode(int seen) {
        ArrayData newUserValueToSet = arrayDataHandler.afterOpcode(stack, seen);

        super.afterOpcode(seen);

        if (newUserValueToSet != null) {
            Item createdArray = stack.getStackItem(0);
            createdArray.setUserValue(newUserValueToSet);
        }
        throwableHandler.afterOpcode(this, seen);
    }
}
