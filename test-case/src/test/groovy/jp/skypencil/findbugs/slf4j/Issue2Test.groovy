package jp.skypencil.findbugs.slf4j

import org.junit.Test

class Issue2Test {
  @Test
  void testToFindPlaceHolderMismatch() {
    new XmlParser().expect(pkg.Issue2, [
      'SLF4J_PLACE_HOLDER_MISMATCH': 1
    ]);
  }
}