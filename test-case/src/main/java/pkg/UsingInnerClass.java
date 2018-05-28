package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingInnerClass {
  static final class InnerClass {}

  private final Logger logger = LoggerFactory.getLogger(InnerClass.class);

  void method() {
    logger.info("hello, world");
    LoggerFactory.getLogger(InnerClass.class).info("Hello, world");
  }
}
