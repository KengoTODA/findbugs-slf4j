package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingNonFinalLogger {
  private Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("Hello, world");
    LoggerFactory.getLogger(getClass()).info("Hello, world");
  }
}
