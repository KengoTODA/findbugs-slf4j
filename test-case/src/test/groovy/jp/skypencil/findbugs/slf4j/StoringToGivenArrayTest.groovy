package jp.skypencil.findbugs.slf4j

import org.junit.Test

class StoringToGivenArrayTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.StoringToGivenArray, [:])
  }
}
