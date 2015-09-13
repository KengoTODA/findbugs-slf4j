package jp.skypencil.findbugs.slf4j;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class Issue35Test {
  @Test
  public void testToFindFalsePositive() {
    Map<String, Integer> expectedError = Maps.newHashMap();
    expectedError.put("SLF4J_PLACE_HOLDER_MISMATCH", 2);
    expectedError.put("SLF4J_FORMAT_SHOULD_BE_CONST", 4);
    new XmlParser().expect(pkg.Issue35.class, expectedError);
  }
}