package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class Issue2Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
      Map<String, Integer> expectedError = Collections.singletonMap("SLF4J_PLACE_HOLDER_MISMATCH", 1);
    new XmlParser().expect(pkg.Issue2.class, expectedError);
  }
}