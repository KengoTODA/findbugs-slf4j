package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingTwoPlaceHolderWithOneParameter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("we are {} {}", "using");
    LoggerFactory.getLogger(getClass()).info("we are {} {}", "using");
  }
}
