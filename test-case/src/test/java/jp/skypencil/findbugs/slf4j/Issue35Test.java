package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue35Test {
  @Test
  public void testToFindFalsePositive() {
    Map<String, Integer> expectedError = Maps.newHashMap();
    expectedError.put("SLF4J_PLACE_HOLDER_MISMATCH", 2);
    expectedError.put("SLF4J_FORMAT_SHOULD_BE_CONST", 4);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.Issue35.class, expectedError);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_PLACE_HOLDER_MISMATCH",
            "Count of placeholder(2) is not equal to count of parameter(1)");
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_PLACE_HOLDER_MISMATCH",
            "Count of placeholder(1) is not equal to count of parameter(0)");
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_FORMAT_SHOULD_BE_CONST",
            "Format should be constant. Use placeholder to reduce the needless cost of parameter construction. see http://www.slf4j.org/faq.html#logging_performance");
  }
}
