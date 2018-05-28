package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingOnePlaceHolder {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("using {}", "placeholder");
    LoggerFactory.getLogger(getClass()).info("using {}", "placeholder");
  }
}
