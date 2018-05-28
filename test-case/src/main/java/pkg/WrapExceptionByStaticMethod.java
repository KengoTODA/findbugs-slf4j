package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrapExceptionByStaticMethod {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void makingExceptionByStaticMethod() {
    RuntimeException e = makeExceptionStatic();
    logger.error("Error ocurred", e);
    logger.error("Error {}", "ocurred", e);
  }

  private static RuntimeException makeExceptionStatic() {
    return new RuntimeException();
  }
}
