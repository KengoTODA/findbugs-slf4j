package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingConcatedStringAsFormatTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_FORMAT_SHOULD_BE_CONST", 4);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingConcatedStringAsFormat.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_FORMAT_SHOULD_BE_CONST",
            "Format should be constant. Use placeholder to reduce the needless cost of parameter construction. see http://www.slf4j.org/faq.html#logging_performance");
  }
}
