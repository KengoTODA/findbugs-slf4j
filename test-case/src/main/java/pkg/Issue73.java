package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @see https://github.com/KengoTODA/findbugs-slf4j/issues/73 */
public class Issue73 {
  private final Logger log = LoggerFactory.getLogger(getClass());

  Thread.UncaughtExceptionHandler method() {
    Thread.UncaughtExceptionHandler handler = (t, e) -> log.error("my logging message", e);
    return handler;
  }
}
