package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingExceptionFromStaticField {
  private static Exception exception;
  private static Object object;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  static void setException(Exception exception) {
    UsingExceptionFromStaticField.exception = exception;
  }

  static void setObject(Object object) {
    UsingExceptionFromStaticField.object = object;
  }

  void method() {
    logger.info("Hello, exception", exception);
    logger.info("Hello, exception", new Object[] {exception});
    logger.info("Hello, {}", object);
    logger.info("Hello, {}", new Object[] {object});
  }
}
