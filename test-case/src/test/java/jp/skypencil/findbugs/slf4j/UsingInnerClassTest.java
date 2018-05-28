package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingInnerClassTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_ILLEGAL_PASSED_CLASS", 2);
    Multimap<String, String> longMessages =
        new XmlParser().expect(pkg.UsingInnerClass.class, expected);
    assertThat(longMessages)
        .containsEntry(
            "SLF4J_ILLEGAL_PASSED_CLASS",
            "Illegal class is passed to LoggerFactory#getLogger(Class). It should be one of [pkg.UsingInnerClass].");
  }
}
