package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingCaughtExceptionToLogTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingCaughtExceptionToLog.class, Collections.emptyMap());
  }
}
