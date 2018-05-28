package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingGivenArrayAsParameterTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_UNKNOWN_ARRAY", 2);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingGivenArrayAsParameter.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_UNKNOWN_ARRAY",
            "Using unknown array as parameter. You cannot use array which is provided as method argument or returned from other method, for better static analysis by findbugs-slf4j.");
  }
}
