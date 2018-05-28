package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue31Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    Map<String, Integer> expectedError = Collections.emptyMap();
    new XmlParser().expect(pkg.Issue31.class, expectedError);
  }
}
