package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingExceptionFromField {
  private Exception exception;
  private Object object;
  private int integer;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  void setException(Exception exception) {
    this.exception = exception;
  }

  void setObject(Object object) {
    this.object = object;
  }

  void setInteger(int integer) {
    this.integer = integer;
  }

  void method() {
    logger.info("Hello, exception", exception);
    logger.info("Hello, exception", new Object[] {exception});
    logger.info("Hello, {}", object);
    logger.info("Hello, {}", new Object[] {object});
    logger.info("Hello, {}", integer);
    logger.info("Hello, {}", new Object[] {integer});
  }
}
