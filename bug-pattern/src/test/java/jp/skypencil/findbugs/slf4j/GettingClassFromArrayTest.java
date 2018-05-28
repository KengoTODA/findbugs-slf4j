package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpotBugsExtension.class)
public class GettingClassFromArrayTest {
  @Test
  public void test(SpotBugsRunner spotbugs) {
    BugCollection bugs =
        spotbugs.performAnalysis(Paths.get("target/test-classes/pkg/GettingClassFromArray.class"));
    assertThat(bugs).hasSize(1);

    BugInstance bug = bugs.getCollection().stream().findFirst().orElseThrow(AssertionError::new);
    assertThat(bug.getType()).isEqualTo("UC_USELESS_VOID_METHOD");
  }
}
