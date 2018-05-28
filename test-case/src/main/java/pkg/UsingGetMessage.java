package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingGetMessage {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method(Throwable t) {
    logger.info("My message is {}", t.getMessage(), t);
    logger.info("My {} is {}", new Object[] {"message", t.getMessage(), t});
    logger.info("My {} {} {}", new Object[] {"message", "is", t.getMessage(), t});
  }
}
