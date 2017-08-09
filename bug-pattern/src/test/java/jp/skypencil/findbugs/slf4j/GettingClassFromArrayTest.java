package jp.skypencil.findbugs.slf4j;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

public class GettingClassFromArrayTest {
  @Rule
  public SpotBugsRule spotbugs = new SpotBugsRule();

  @Test
  public void test() {
    BugCollection bugs = spotbugs.performAnalysis(Paths.get("target/test-classes/pkg/GettingClassFromArray.class"));
    BugInstanceMatcher matcher = new BugInstanceMatcherBuilder().bugType("UC_USELESS_VOID_METHOD").build();
    assertThat(bugs, containsExactly(1, matcher));
  }
}
