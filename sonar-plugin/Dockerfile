# After `mvn clean package` in the project root dir, run following command to build:
# docker image build . -t findbugs-slf4j
#
# Then you can run SonarQube instance with findbugs-slf4j:
# docker container run -it -P findbugs-slf4j

FROM sonarqube:6.7.5

ENV SONAR_JAVA_VERSION=5.1.0.13090 \
    SONAR_FINDBUGS_VERSION=3.8.0 \
    FINDBUGS_SLF4J_VERSION=1.5.0-SNAPSHOT

RUN wget -P $SONARQUBE_HOME/extensions/plugins/ --no-verbose https://github.com/spotbugs/sonar-findbugs/releases/download/$SONAR_FINDBUGS_VERSION/sonar-findbugs-plugin-$SONAR_FINDBUGS_VERSION.jar
RUN wget -P $SONARQUBE_HOME/extensions/plugins/ --no-verbose http://central.maven.org/maven2/org/sonarsource/java/sonar-java-plugin/$SONAR_JAVA_VERSION/sonar-java-plugin-$SONAR_JAVA_VERSION.jar
COPY target/sonar-findbugs-slf4j-plugin-$FINDBUGS_SLF4J_VERSION.jar $SONARQUBE_HOME/extensions/plugins/
