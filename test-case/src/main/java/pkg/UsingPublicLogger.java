package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingPublicLogger {
  public final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("using log message");
    LoggerFactory.getLogger(getClass()).info("using log message");
  }
}
