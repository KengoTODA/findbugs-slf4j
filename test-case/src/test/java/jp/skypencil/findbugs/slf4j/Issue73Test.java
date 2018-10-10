package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Run this test by {@code mvn clean verify -T 1C -Dspotbugs.fork=false}.
 */
public class Issue73Test {
  @Test
  public void test() {
    Map<String, Integer> expectedError = Collections.emptyMap();
    new XmlParser().expect(pkg.Issue73.class, expectedError);
  }
}
