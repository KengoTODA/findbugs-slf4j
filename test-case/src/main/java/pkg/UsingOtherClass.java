package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingOtherClass {
  private final Logger logger = LoggerFactory.getLogger(String.class);

  void method() {
    logger.info("hello, world");
    LoggerFactory.getLogger(String.class).info("Hello, world");
  }
}
