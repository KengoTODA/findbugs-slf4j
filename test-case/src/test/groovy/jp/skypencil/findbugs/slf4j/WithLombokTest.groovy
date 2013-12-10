package jp.skypencil.findbugs.slf4j

import org.junit.Test

class WithLombokTest {
  @Test
  void testToFindPlaceHolderMismatch() {
    new XmlParser().expect(pkg.WithLombok, [
      'SLF4J_LOGGER_SHOULD_BE_NON_STATIC': 1
    ]);
  }
}