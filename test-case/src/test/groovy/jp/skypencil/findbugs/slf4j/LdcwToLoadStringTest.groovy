package jp.skypencil.findbugs.slf4j

import org.junit.Test

class LdcwToLoadStringTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.LdcwToLoadString, [:])
  }
}
