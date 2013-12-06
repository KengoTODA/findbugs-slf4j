package pkg;

import java.util.Arrays;
import java.util.Set;

/**
 * @see https://github.com/eller86/findbugs-slf4j/issues/29
 */
public class Issue29 {

    public String[] method0(Set<String> set) {
        return set.toArray(new String[set.size()]);
    }

    public void method1() {
        Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    public void method2() {
        Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
    }

}
