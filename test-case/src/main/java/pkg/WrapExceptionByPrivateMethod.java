package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrapExceptionByPrivateMethod {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void makingExceptionByPrivateMethod() {
    RuntimeException e = makeException();
    logger.error("Error ocurred", e);
    logger.error("Error {}", "ocurred", e);
  }

  private RuntimeException makeException() {
    return new RuntimeException();
  }
}
