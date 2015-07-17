package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class WrapExceptionByPrivateMethodTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.WrapExceptionByPrivateMethod.class, Collections.emptyMap());
  }
}