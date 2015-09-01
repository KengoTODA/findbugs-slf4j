package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class Issue35Test {
  @Test
  public void testToFindFalsePositive() {
    Map<String, Integer> expectedError = Collections.singletonMap("SLF4J_FORMAT_SHOULD_BE_CONST", 4);
    new XmlParser().expect(pkg.Issue35.class, expectedError);
  }
}