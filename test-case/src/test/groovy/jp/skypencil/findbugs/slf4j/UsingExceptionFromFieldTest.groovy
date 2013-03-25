package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingExceptionFromFieldTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingExceptionFromField, [:])
  }
}
