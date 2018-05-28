package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingPublicLoggerTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_LOGGER_SHOULD_BE_PRIVATE", 1);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingPublicLogger.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_LOGGER_SHOULD_BE_PRIVATE",
            "To prevent illegal usage, logger should be private field. Change this field (logger) to private field.");
  }
}
