package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingReturnedArrayAsParameter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("Hello, {}.", createParameter());
    LoggerFactory.getLogger(getClass()).info("Hello, {}.", createParameter());
  }

  Object[] createParameter() {
    return new Object[] {"World"};
  }
}
