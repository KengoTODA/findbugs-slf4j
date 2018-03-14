package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Const.INVOKEVIRTUAL;

import com.google.common.base.Objects;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import jp.skypencil.findbugs.slf4j.parameter.AbstractExtendedDetectorForParameterArray;

/**
 * FindBugs (now SpotBugs) Detector for (ab)use of {@link Throwable#getStackTrace()} in SFL4j logger.
 *
 * @author Michael Vorburger.ch
 */
public class ManualGetStackTraceDetector extends AbstractExtendedDetectorForParameterArray {

    @Item.SpecialKind
    private final int getStackTraceKind = Item.defineNewSpecialKind("use of throwable getStackTrace");

    public ManualGetStackTraceDetector(BugReporter bugReporter) {
        super(bugReporter, "SLF4J_GET_STACK_TRACE");
    }

    @Override
    @Item.SpecialKind
    protected int getKindOfInterest() {
        return getStackTraceKind;
    }

    @Override
    protected boolean isWhatWeWantToDetect(int seen) {
        return seen == INVOKEVIRTUAL
                && !stack.isTop()
                && getThrowableHandler().checkThrowable(getStack().getStackItem(0))
                && Objects.equal("getStackTrace", getNameConstantOperand());
    }

}
