package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingNonConstantAsFormat {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("using {}".toUpperCase(), "placeholder");
    LoggerFactory.getLogger(getClass()).info("using {}".toUpperCase(), "placeholder");
  }
}
