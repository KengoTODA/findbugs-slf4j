package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingExceptionFromFieldTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingExceptionFromField.class, Collections.emptyMap());
  }
}
