package pkg;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to reproduce issue 14.
 *
 * @see https://github.com/KengoTODA/findbugs-slf4j/issues/14
 */
public class Issue14 {
  private final Logger LOGGER = LoggerFactory.getLogger(Issue14.class);

  public List<Long> handle(List<Long> requests) {
    List<Long> result = new ArrayList<Long>();
    result.add(1L);
    for (Long request : requests) {
      try {
        result.add(request);
      } catch (RuntimeException e) {
        if (e instanceof ClassCastException || e.getCause() instanceof NullPointerException) {
          LOGGER.error("Failed to process request={}, moving to next ", request.toString(), e);
        } else {
          throw e;
        }
      }
    }
    return result;
  }
}
