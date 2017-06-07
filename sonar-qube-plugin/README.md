# How to use findbugs-slf4j with sonar

To use with your [Sonar](http://www.sonarqube.org/), put `.jar` file and [`rules.xml`](rules.xml) to `${SONAR_HOME}/extensions/rules/findbugs` directory.

You can download `.jar` file from [Maven central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22jp.skypencil.findbugs.slf4j%22%20AND%20a%3A%22bug-pattern%22).

# findbugs-slf4j does not support SonarQube

SonarQube (Sonar ver. 4.2 or later) does not support the above method.
