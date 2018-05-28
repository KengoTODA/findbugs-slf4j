package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingGetMessageTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_MANUALLY_PROVIDED_MESSAGE", 3);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingGetMessage.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_MANUALLY_PROVIDED_MESSAGE",
            "Do not have to use message returned from Throwable#getMessage and Throwable#getLocalizedMessage. It is enough to provide throwable instance as the last argument, then binding will log its message.");
  }
}
