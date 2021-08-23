package pkg;

/** @see https://github.com/KengoTODA/findbugs-slf4j/issues/19 */
public class GettingClassFromArray {
  @SuppressWarnings("ReturnValueIgnored")
  void method() {
    new String[] {}.getClass();
  }
}
