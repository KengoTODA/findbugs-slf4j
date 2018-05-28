package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingExceptionFromArgumentTest {
  @Test
  public void test() {
    // it extends Exception, but it also has Logger field
    Map<String, Integer> expected = Collections.singletonMap("SE_BAD_FIELD", 1);

    new XmlParser().expect(pkg.UsingExceptionFromArgument.class, expected);
  }
}
