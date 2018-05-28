package pkg;

/** @see https://github.com/KengoTODA/findbugs-slf4j/issues/37 */
public class Issue37 {
  String method() {
    String[] strings = {};
    String string = "";
    for (int i = 0; i < strings.length; ++i) {
      string += strings[i];
    }
    return string;
  }
}
