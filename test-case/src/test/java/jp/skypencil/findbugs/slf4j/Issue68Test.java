package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue68Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    Map<String, Integer> expectedError =
        Collections.singletonMap("SLF4J_PLACE_HOLDER_MISMATCH", 2);
    new XmlParser().expect(pkg.Issue68.class, expectedError);
  }
}
