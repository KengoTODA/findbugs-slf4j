package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link ManualGetStackTraceDetector}.
 *
 * @author Michael Vorburger.ch
 */
public class UsingGetStackTraceTest {

    @Test
    public void testExceptionMethods() throws Exception {
        Map<String, Integer> expected = Collections.singletonMap("SLF4J_GET_STACK_TRACE", 1);
        Multimap<String, String> longMessages = new XmlParser().expect(pkg.UsingGetStackTrace.class, expected);
        assertThat(longMessages).containsEntry("SLF4J_GET_STACK_TRACE",
                "Do not have use Throwable#getStackTrace. It is enough to provide throwable instance as the last argument, then binding will log its message.");
    }

}
