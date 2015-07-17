package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class WrapExceptionByStaticMethodTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.WrapExceptionByStaticMethod.class, Collections.emptyMap());
  }
}