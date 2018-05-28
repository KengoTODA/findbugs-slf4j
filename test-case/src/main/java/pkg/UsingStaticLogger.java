package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingStaticLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(UsingStaticLogger.class);

  void method() {
    LOGGER.info("Hello, world");
  }
}
