package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingGetLocalizedMessage {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method(Throwable t) {
    logger.info("My message is {}", t.getLocalizedMessage(), t);
    logger.info("My {} is {}", new Object[] {"message", t.getLocalizedMessage(), t});
    logger.info("My {} {} {}", new Object[] {"message", "is", t.getLocalizedMessage(), t});
  }
}
