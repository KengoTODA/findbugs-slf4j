package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingOtherClassTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_ILLEGAL_PASSED_CLASS", 2);
    new XmlParser().expect(pkg.UsingOtherClass.class, expected);
  }
}
