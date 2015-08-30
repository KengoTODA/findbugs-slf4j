package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class Issue35Test {
  @Test
  public void testToFindFalsePositive() {
      Map<String, Integer> expectedError = Collections.emptyMap();
    new XmlParser().expect(pkg.Issue35.class, expectedError);
  }
}