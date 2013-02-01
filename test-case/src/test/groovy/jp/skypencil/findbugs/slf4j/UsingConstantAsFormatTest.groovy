package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingConstantAsFormatTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingConstantAsFormat, [:])
  }
}
