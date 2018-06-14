package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Const.INVOKEVIRTUAL;

import com.google.common.base.Objects;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import javax.annotation.Nullable;
import jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray;
import jp.skypencil.findbugs.slf4j.parameter.ArrayData;
import jp.skypencil.findbugs.slf4j.parameter.ArrayDataHandler.Strategy;

@CustomUserValue
public final class ManualMessageDetector extends AbstractDetectorForParameterArray {
  @Item.SpecialKind
  private final int isMessage = Item.defineNewSpecialKind("message generated by throwable object");

  public ManualMessageDetector(BugReporter bugReporter) {
    super(bugReporter);
  }

  @Override
  protected Strategy createArrayCheckStrategy() {
    return new Strategy() {
      @Override
      public void store(Item storedItem, ArrayData arrayData, int index) {
        if (arrayData == null) {
          return;
        }

        if (storedItem.getSpecialKind() == isMessage) {
          arrayData.mark(true);
        }

        // Let developer logs exception message, only when argument does not have throwable instance
        // https://github.com/KengoTODA/findbugs-slf4j/issues/31
        if (index == arrayData.getSize() - 1 && !getThrowableHandler().checkThrowable(storedItem)) {
          arrayData.mark(false);
        }
      }
    };
  }

  @Override
  public void afterOpcode(int seen) {
    boolean isInvokingGetMessage = isInvokingGetMessage(seen);
    super.afterOpcode(seen);

    if (isInvokingGetMessage && !stack.isTop()) {
      stack.getStackItem(0).setSpecialKind(isMessage);
    }
  }

  private boolean isInvokingGetMessage(int seen) {
    return seen == INVOKEVIRTUAL
        && !stack.isTop()
        && getThrowableHandler().checkThrowable(getStack().getStackItem(0))
        && (Objects.equal("getMessage", getNameConstantOperand())
            || Objects.equal("getLocalizedMessage", getNameConstantOperand()));
  }

  @Override
  protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
    if (arrayData == null || !arrayData.isMarked()) {
      return;
    }
    BugInstance bugInstance =
        new BugInstance(this, "SLF4J_MANUALLY_PROVIDED_MESSAGE", NORMAL_PRIORITY)
            .addSourceLine(this)
            .addClassAndMethod(this)
            .addCalledMethod(this);
    getBugReporter().reportBug(bugInstance);
  }
}
