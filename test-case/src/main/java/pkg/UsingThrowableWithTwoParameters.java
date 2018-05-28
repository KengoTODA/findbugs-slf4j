package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingThrowableWithTwoParameters {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("we are {} {}", new Object[] {"using", "2 parameters", new RuntimeException()});
    LoggerFactory.getLogger(getClass())
        .info("we are {} {}", new Object[] {"using", "2 parameters", new RuntimeException()});
  }
}
