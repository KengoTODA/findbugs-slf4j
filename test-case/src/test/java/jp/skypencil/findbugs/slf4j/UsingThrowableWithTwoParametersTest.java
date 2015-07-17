package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingThrowableWithTwoParametersTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingThrowableWithTwoParameters.class, Collections.emptyMap());
  }
}
