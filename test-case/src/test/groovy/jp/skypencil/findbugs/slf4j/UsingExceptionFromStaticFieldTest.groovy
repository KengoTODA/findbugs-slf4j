package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingExceptionFromStaticFieldTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingExceptionFromStaticField, [:])
  }
}
