package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingThrowableWithParameter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("Hello, {}", "world", new RuntimeException());
    LoggerFactory.getLogger(getClass()).info("Hello, {}", "world", new RuntimeException());
  }
}
