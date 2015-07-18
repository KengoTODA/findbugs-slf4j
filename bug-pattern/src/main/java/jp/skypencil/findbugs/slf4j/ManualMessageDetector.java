package jp.skypencil.findbugs.slf4j;

import javax.annotation.Nullable;

import jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray;
import jp.skypencil.findbugs.slf4j.parameter.ArrayData;
import jp.skypencil.findbugs.slf4j.parameter.ArrayDataHandler.Strategy;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.OpcodeStack.Item;

@CustomUserValue
public final class ManualMessageDetector extends AbstractDetectorForParameterArray {
    private static final String IS_MESSAGE = "IS_MESSAGE";

    public ManualMessageDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected Strategy createArrayCheckStrategy() {
        return new Strategy() {
            @Override
            public void store(Item storedItem, ArrayData arrayData, int index) {
                if (IS_MESSAGE.equals(storedItem.getUserValue())) {
                    arrayData.mark(true);
                }
            }
        };
    }


    @Override
    public void afterOpcode(int seen) {
        if (getStack().isTop()) {
            // see https://github.com/KengoTODA/findbugs-slf4j/issues/29
            System.err.printf("ManualMessageDetector: stack is TOP, cannot be analyzed. %s:%d%n",
                    getClassName(), getPC());
            super.afterOpcode(seen);
            return;
        }

        boolean isInvokingGetMessage = isInvokingGetMessage(seen);
        super.afterOpcode(seen);

        if (isInvokingGetMessage) {
            getStack().getStackItem(0).setUserValue(IS_MESSAGE);
        }
    }

    private boolean isInvokingGetMessage(int seen) {
        return seen == INVOKEVIRTUAL
                && getThrowableHandler().checkThrowable(getStack().getStackItem(0))
                && (Objects.equal("getMessage", getNameConstantOperand()) ||
                        Objects.equal("getLocalizedMessage", getNameConstantOperand()));
    }

    @Override
    protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
        if (arrayData == null || !arrayData.isMarked()) {
            return;
        }
        BugInstance bugInstance = new BugInstance(this,
                "SLF4J_MANUALLY_PROVIDED_MESSAGE", NORMAL_PRIORITY)
                .addSourceLine(this).addClassAndMethod(this)
                .addCalledMethod(this);
        getBugReporter().reportBug(bugInstance);
    }
}
