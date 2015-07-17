package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingOuterClassTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingOuterClass.class, Collections.emptyMap());
  }
}
