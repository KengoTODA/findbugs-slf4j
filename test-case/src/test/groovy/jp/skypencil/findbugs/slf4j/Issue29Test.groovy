package jp.skypencil.findbugs.slf4j

import org.junit.Test

class Issue29Test {
  @Test
  void testToFindPlaceHolderMismatch() {
    new XmlParser().expect(pkg.Issue29, [
      'DLS_DEAD_LOCAL_STORE': 1,
      'SLF4J_SIGN_ONLY_FORMAT': 1,
      'SLF4J_MANUALLY_PROVIDED_MESSAGE': 1,
      'UC_USELESS_VOID_METHOD': 1,
      'UC_USELESS_OBJECT': 1
    ]);
  }
}
