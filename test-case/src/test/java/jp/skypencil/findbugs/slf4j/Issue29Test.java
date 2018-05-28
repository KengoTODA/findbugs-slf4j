package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;

public class Issue29Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
    Multimap<String, String> longMessages =
        new XmlParser()
            .expect(
                pkg.Issue29.class,
                builder
                    .put("DLS_DEAD_LOCAL_STORE", 1)
                    .put("SLF4J_SIGN_ONLY_FORMAT", 1)
                    .put("SLF4J_MANUALLY_PROVIDED_MESSAGE", 1)
                    .put("UC_USELESS_VOID_METHOD", 1)
                    .put("UC_USELESS_OBJECT", 1)
                    .build());
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_SIGN_ONLY_FORMAT",
            "To make log readable, log format ({}) should contain non-sign character.");
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_MANUALLY_PROVIDED_MESSAGE",
            "Do not have to use message returned from Throwable#getMessage and Throwable#getLocalizedMessage. It is enough to provide throwable instance as the last argument, then binding will log its message.");
  }
}
