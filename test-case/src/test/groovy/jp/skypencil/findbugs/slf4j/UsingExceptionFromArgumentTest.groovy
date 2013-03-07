package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingExceptionFromArgumentTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingExceptionFromArgument, [
      'SE_BAD_FIELD': 1  // it extends Exception, but it also has Logger field
    ])
  }
}
