package jp.skypencil.findbugs.slf4j

import org.junit.Test

class WrapExceptionByPrivateMethodTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.WrapExceptionByPrivateMethod, [:]);
  }
}