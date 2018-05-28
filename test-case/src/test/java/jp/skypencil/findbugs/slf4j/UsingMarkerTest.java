package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingMarkerTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("NP_LOAD_OF_KNOWN_NULL_VALUE", 1);
    new XmlParser().expect(pkg.UsingMarker.class, expected);
  }
}
