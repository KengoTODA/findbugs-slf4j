package jp.skypencil.findbugs.slf4j;

import com.youdevise.fbplugins.tdd4fb.DetectorAssert;
import edu.umd.cs.findbugs.BugReporter;
import org.junit.jupiter.api.Test;
import pkg.Issue14;

public class DebuggingIssue14 {
  @Test
  public void test() throws Exception {
    BugReporter bugReporter = DetectorAssert.bugReporterForTesting();
    WrongPlaceholderDetector detector = new WrongPlaceholderDetector(bugReporter);
    DetectorAssert.assertNoBugsReported(Issue14.class, detector, bugReporter);
  }
}
