package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingSignOnlyTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_SIGN_ONLY_FORMAT", 2);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingSignOnly.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_SIGN_ONLY_FORMAT",
            "To make log readable, log format ({}) should contain non-sign character.");
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_SIGN_ONLY_FORMAT",
            "To make log readable, log format ({}, {}) should contain non-sign character.");
  }
}
