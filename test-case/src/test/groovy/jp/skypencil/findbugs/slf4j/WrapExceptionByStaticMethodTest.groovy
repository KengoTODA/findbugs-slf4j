package jp.skypencil.findbugs.slf4j

import org.junit.Test

class WrapExceptionByStaticMethodTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.WrapExceptionByStaticMethod, [:]);
  }
}