package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingJapaneseTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingJapanese, [:])
  }
}
