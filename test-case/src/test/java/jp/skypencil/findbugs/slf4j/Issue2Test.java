package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue2Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    Map<String, Integer> expectedError = Collections.singletonMap("SLF4J_PLACE_HOLDER_MISMATCH", 1);
    Multimap<String, String> longMessages = new XmlParser().expect(pkg.Issue2.class, expectedError);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_PLACE_HOLDER_MISMATCH",
            "Count of placeholder(4) is not equal to count of parameter(1)");
  }
}
