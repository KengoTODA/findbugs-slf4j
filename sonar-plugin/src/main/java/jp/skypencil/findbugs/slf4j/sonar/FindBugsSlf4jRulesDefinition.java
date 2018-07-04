package jp.skypencil.findbugs.slf4j.sonar;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.java.Java;

public class FindBugsSlf4jRulesDefinition implements RulesDefinition {
  public static final String REPOSITORY_KEY = "findbugs-slf4j";
  public static final String REPOSITORY_NAME = "FindBugsSlf4j";
  public static final int RULE_COUNT = 10;

  @Override
  public void define(Context context) {
    NewRepository repository =
        context.createRepository(REPOSITORY_KEY, Java.KEY).setName(REPOSITORY_NAME);

    RulesDefinitionXmlLoader ruleLoader = new RulesDefinitionXmlLoader();
    ruleLoader.load(
        repository,
        FindBugsSlf4jRulesDefinition.class.getResourceAsStream(
            "/jp/skypencil/findbugs/slf4j/sonar/rules.xml"),
        "UTF-8");
    repository.done();
  }
}
