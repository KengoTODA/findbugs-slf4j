package jp.skypencil.findbugs.slf4j;

import org.junit.Test;

import pkg.Issue14;

import com.youdevise.fbplugins.tdd4fb.DetectorAssert;

import edu.umd.cs.findbugs.BugReporter;

public class DebuggingIssue14 {
    @Test
    public void test() throws Exception {
        BugReporter bugReporter = DetectorAssert.bugReporterForTesting();
        WrongPlaceholderDetector detector = new WrongPlaceholderDetector(bugReporter);
        DetectorAssert.assertNoBugsReported(Issue14.class, detector, bugReporter);
    }
}
