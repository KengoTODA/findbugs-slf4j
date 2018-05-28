package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingConcatedStringAsFormat {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("This {} is in " + getClass().getName(), "method");
    LoggerFactory.getLogger(getClass()).info("This {} is in " + getClass().getName(), "method");

    // this rule should check #method(String), which has no parameter
    logger.info("This class is " + getClass().getName());
    LoggerFactory.getLogger(getClass()).info("This class is " + getClass().getName());
  }
}
