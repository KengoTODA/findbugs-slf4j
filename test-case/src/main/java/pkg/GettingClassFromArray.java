package pkg;

/**
 * @see https://github.com/eller86/findbugs-slf4j/issues/19
 */
public class GettingClassFromArray {
    void method() {
        new String[]{}.getClass();
    }
}
