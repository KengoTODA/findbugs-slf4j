package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingReturnedArrayAsParameterTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_UNKNOWN_ARRAY", 2);
    new XmlParser().expect(pkg.UsingReturnedArrayAsParameter.class, expected);
  }
}
