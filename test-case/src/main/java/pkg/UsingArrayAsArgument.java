package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UsingArrayAsArgument {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void reportedPattern(Object[] args) {
    logger.info("Logging int array {}.", int[].class);
  }

  void additionalPattern(Object[] args) {
    logger.info("Logging int[] array {}.", int[][].class);
    logger.info("Logging Object array {}.", Object[].class);
  }
}
