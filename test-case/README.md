# TEST CASE FOR INTEGRATION-TEST

This project provides test cases for integration test.
Please execute `mvn clean install` at root directory to use this project.

## Steps

1. This project depends on bug-pattern sub project. So FindBugs plugin will be installed to your Maven repository before build for this project starts.
2. `compile` phase puts class files into /target directory.
3. `pre-integration-test` phase executes FindBugs with SLF4J plugin.
4. At last, `integration-test` phase fires maven-surefile-plugin to test.

## How to add new test case

1. Create a minimum Java class in `src/main/java` to reproduce specific situation
2. Create a JUnit test case in `src/test/java`. Use MessageOnlyTest.java as example.
