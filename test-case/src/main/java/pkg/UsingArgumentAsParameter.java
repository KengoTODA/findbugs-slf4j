package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingArgumentAsParameter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method(Object[] args) {
    logger.info("Hello, {}.", args);
    LoggerFactory.getLogger(getClass()).info("Hello, {}.", args);
  }
}
