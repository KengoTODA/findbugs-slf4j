package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingStaticLoggerTest {
  @Test
  public void test() {
    Map<String, Integer> expected =
        Collections.singletonMap("SLF4J_LOGGER_SHOULD_BE_NON_STATIC", 1);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingStaticLogger.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_LOGGER_SHOULD_BE_NON_STATIC",
            "Logger should be non-static field. Change this field (LOGGER) to non-static field.");
  }
}
