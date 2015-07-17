package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class GettingClassFromArrayTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("UC_USELESS_VOID_METHOD", 1);
    new XmlParser().expect(pkg.GettingClassFromArray.class, expected);
  }
}
