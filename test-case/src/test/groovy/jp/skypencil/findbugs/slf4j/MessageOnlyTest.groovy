package jp.skypencil.findbugs.slf4j

import jp.skypencil.findbugs.slf4j.XmlParser;

import org.junit.Test

/**
 * A simple test case to explain how to use {@link XmlParser} to test.
 * @author eller86
 */
class MessageOnlyTest {
  @Test
  void test() {
      new XmlParser().expect(pkg.MessageOnly, [:]);
    
  }
}