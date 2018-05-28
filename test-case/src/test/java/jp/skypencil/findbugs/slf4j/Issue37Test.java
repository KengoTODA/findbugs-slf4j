package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue37Test {
  @Test
  public void testToFindIllegalArgumentException() {
    Map<String, Integer> expectedError =
        Collections.singletonMap("SBSC_USE_STRINGBUFFER_CONCATENATION", 1);
    new XmlParser().expect(pkg.Issue37.class, expectedError);
  }
}
