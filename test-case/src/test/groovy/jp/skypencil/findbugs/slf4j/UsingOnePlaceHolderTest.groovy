package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingOnePlaceHolderTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingOnePlaceHolder, [:])
  }
}
