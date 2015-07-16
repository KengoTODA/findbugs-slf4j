package jp.skypencil.findbugs.slf4j

import org.junit.Test

class GettingClassFromArrayTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.GettingClassFromArray, [
      'UC_USELESS_VOID_METHOD': 1
    ]);
  }
}
