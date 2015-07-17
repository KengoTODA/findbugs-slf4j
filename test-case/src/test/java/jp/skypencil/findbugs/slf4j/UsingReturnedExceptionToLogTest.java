package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingReturnedExceptionToLogTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingReturnedExceptionToLog.class, Collections.emptyMap());
  }
}
