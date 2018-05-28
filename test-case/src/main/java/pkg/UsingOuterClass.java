package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingOuterClass {
  static final class InnerClass {
    private final Logger logger = LoggerFactory.getLogger(UsingOuterClass.class);

    void method() {
      logger.info("hello, world");
      LoggerFactory.getLogger(UsingOuterClass.class).info("Hello, world");
      LoggerFactory.getLogger(InnerClass.class).info("Hello, world");
    }
  }
}
