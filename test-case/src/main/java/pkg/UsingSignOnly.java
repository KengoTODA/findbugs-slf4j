package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingSignOnly {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("{}, {}", "Hello", "world");
    log("{}", "Hello");
  }

  private void log(String format, Object data) {
    logger.debug(format, data);
  }
}
