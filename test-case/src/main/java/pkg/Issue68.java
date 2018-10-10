package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @see https://github.com/KengoTODA/findbugs-slf4j/issues/68 */
public class Issue68 {
  private final Logger log = LoggerFactory.getLogger(getClass());

  void method() {
    log.info("I have a placeholder but no parameter. {}");
    log.info("I have a placeholder but no parameter. {}", new RuntimeException());
  }
}
