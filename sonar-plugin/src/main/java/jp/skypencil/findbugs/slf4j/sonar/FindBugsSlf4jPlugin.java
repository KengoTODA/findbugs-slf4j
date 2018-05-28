package jp.skypencil.findbugs.slf4j.sonar;

import java.util.Arrays;
import org.sonar.api.Plugin;

public class FindBugsSlf4jPlugin implements Plugin {
  @Override
  public void define(Context context) {
    context.addExtensions(Arrays.asList(FindBugsSlf4jRulesDefinition.class));
  }
}
