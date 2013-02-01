package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingTwoPlaceHolderWithOneParameterTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingTwoPlaceHolderWithOneParameter, [
      'SLF4J_PLACE_HOLDER_MISMATCH': 2
    ])
  }
}
