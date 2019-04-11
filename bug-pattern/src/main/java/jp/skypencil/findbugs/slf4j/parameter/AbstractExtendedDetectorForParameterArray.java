package jp.skypencil.findbugs.slf4j.parameter;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import javax.annotation.Nullable;
import jp.skypencil.findbugs.slf4j.parameter.ArrayDataHandler.Strategy;

public abstract class AbstractExtendedDetectorForParameterArray extends AbstractDetectorForParameterArray {

    private final String bugPatternName;

    public AbstractExtendedDetectorForParameterArray(BugReporter bugReporter, String bugPatternName) {
      super(bugReporter);
      this.bugPatternName = bugPatternName;
    }

    @Override
    protected final Strategy createArrayCheckStrategy() {
      return (storedItem, arrayData, index) -> {
        if (arrayData == null) {
          return;
        }

        if (storedItem.getSpecialKind() == getKindOfInterest()) {
          arrayData.mark(true);
        }

        if (!isReallyOfInterest(storedItem, arrayData, index)) {
          arrayData.mark(false);
        }
      };
    }

    protected boolean isReallyOfInterest(Item storedItem, ArrayData arrayData, int index) {
        return true;
    }

    @Override
    public final void afterOpcode(int seen) {
      boolean isInvokingGetMessage = isWhatWeWantToDetect(seen);
      super.afterOpcode(seen);

      if (isInvokingGetMessage && !stack.isTop()) {
        stack.getStackItem(0).setSpecialKind(getKindOfInterest());
      }
    }

    @Override
    protected final void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
      if (arrayData == null || !arrayData.isMarked()) {
        return;
      }
      BugInstance bugInstance = new BugInstance(this,
            bugPatternName, NORMAL_PRIORITY)
            .addSourceLine(this).addClassAndMethod(this)
            .addCalledMethod(this);
      getBugReporter().reportBug(bugInstance);
    }

    abstract protected boolean isWhatWeWantToDetect(int seen);

    abstract protected int getKindOfInterest();

}
