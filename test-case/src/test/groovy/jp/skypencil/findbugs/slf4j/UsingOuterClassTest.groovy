package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingOuterClassTest {
  @Test
    void test() {
    new XmlParser().expect(pkg.UsingOuterClass, [:])
  }
}
