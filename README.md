# FindBugs bug pattern for SLF4J

This product helps you to verify usage of SLF4J 1.6 and 1.7. Both of Java7 and Java8 are supported.

To use this plugin with Sonar, see [here](sonar-plugin/README.md).

[![Build Status](https://secure.travis-ci.org/KengoTODA/findbugs-slf4j.png)](http://travis-ci.org/KengoTODA/findbugs-slf4j)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jp.skypencil.findbugs.slf4j%3Afindbugs-slf4j&metric=alert_status)](https://sonarcloud.io/dashboard?id=jp.skypencil.findbugs.slf4j%3Afindbugs-slf4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/jp.skypencil.findbugs.slf4j/findbugs-slf4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/jp.skypencil.findbugs.slf4j/findbugs-slf4j)

## Motivation

SLF4J is useful logging facade, but sometimes we mistake how to use. Can you find mistakes in following class? It is not so easy especially in huge product, this FindBugs plugin will help you to find.

```java
class Foo {
    private static final Logger logger = LoggerFactory.getLogger(Bar.class);

    void rethrow(String name, Throwable t) {
        logger.info("Hello, {}!");
        logger.warn("Now I will wrap and throw {}", t);
        throw new RuntimeException(t);
    }
}
```


# Provided bug patterns

Currently this product provides 9 patterns.

## SLF4J_PLACE_HOLDER_MISMATCH

This pattern checks how placeholder is used.
Alert if count of placeholder does not match to count of parameter.

  Note:
  Format should be CONST to use this bug pattern. Use SLF4J_FORMAT_SHOULD_BE_CONST bug pattern to check it.

```java
class Foo {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method() {
        // invalid: this logging method has 2 placeholders, but given parameter is only 1.
        logger.info("{}, {}.", "Hello");

        // valid
        logger.info("{}, {}.", "Hello", "World");

        // invalid: Throwable instance does not need placeholder
        logger.error("{}, {}", "Hello", new RuntimeException());

        // valid
        logger.error("{}", "Hello", new RuntimeException());
    }
}
```

## SLF4J_FORMAT_SHOULD_BE_CONST

This pattern checks given format is CONST or not.
Alert if format is not CONST.

```java
class Foo {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method() {
        // invalid: format is not CONST
        String format = new String("Hello, ");
        logger.info(format + "{}.", "World");

        // valid
        logger.info("Hello, {}.", "World");
    }
}
```

## SLF4J_UNKNOWN_ARRAY

This pattern reports a bug if your code is using array which is provided as method argument or returned from other method.
It makes our verification harder, so please stop using it.

```java
class Foo {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method(Object[] args) {
        // invalid: using method argument as parameter
        logger.info("Hello, {}.", args);

        // valid
        logger.info("Hello, {}.", new Object[]{ "World" });
    }
}
```

## SLF4J_LOGGER_SHOULD_BE_PRIVATE

This pattern reports non private field whose type is org.slf4j.Logger.

```java
class Foo {
    // invalid: field is not private
    public final Logger logger = LoggerFactory.getLogger(getClass());

    // valid
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
```

## SLF4J_LOGGER_SHOULD_BE_FINAL

This pattern reports non final field whose type is org.slf4j.Logger.

```java
class Foo {
    // invalid: field is not final
    private Logger logger = LoggerFactory.getLogger(getClass());

    // valid
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
```

## SLF4J_LOGGER_SHOULD_BE_NON_STATIC

This pattern reports static field whose type is org.slf4j.Logger.

Sometimes using static logger is better than using non-static one. See [official FAQ](http://www.slf4j.org/faq.html#declared_static) for detail.

If you need to use static logger, you can use [PMD's default rule for logger](http://pmd.sourceforge.net/pmd-5.0.0/rules/java/logging-java.html#LoggerIsNotStaticFinal) instead.

```java
class Foo {
    // invalid: field is static
    private static final Logger LOGGER = LoggerFactory.getLogger(Foo.class);

    // valid
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
```

## SLF4J_ILLEGAL_PASSED_CLASS

This pattern reports that illegal class is passed to LoggerFactory.getLogger(Class)

```java
class Foo {
    // invalid: illegal class is passed to Factory
    private final Logger logger = LoggerFactory.getLogger(Bar.class);

    // valid
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger logger = LoggerFactory.getLogger(Foo.class);
}
```

## SLF4J_SIGN_ONLY_FORMAT

This pattern reports that log format which contains only sign and spaces.
To make log readable, you have to use letter to explain your log.

```java
class Foo {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method() {
        // invalid: bad readability
        logger.info("{}", id);

        // valid
        logger.info("{} signed in", userId);
    }
}
```

## SLF4J_MANUALLY_PROVIDED_MESSAGE

This pattern reports needless message which is returned by `Throwable#getMessage()`
or `Throwable#getLocalizedMessage()`. Normally binding will call these methods
when you provide throwable instance as the last argument, so you do not have to
call them manually.

```java
class Foo {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method() {
        // invalid: needless 'e.getMessage()'
        logger.info("Error occured. Message is {}", e.getMessage(), e);

        // valid
        logger.info("Error occured.", e);
    }
}
```

# How to use with Maven

To use this product, please configure your spotbugs-maven-plugin like below.

```xml
      <plugin>
        <groupId>com.github.spotbugs</groupId>
        <artifactId>spotbugs-maven-plugin</artifactId>
        <version>3.1.0-RC8</version>
        <configuration>
          <plugins>
            <plugin>
              <groupId>jp.skypencil.findbugs.slf4j</groupId>
              <artifactId>bug-pattern</artifactId>
              <version>1.4.0</version>
            </plugin>
          </plugins>
        </configuration>
      </plugin>
```

# How to use with Gradle

To use these detectors from a Gradle build, please follow the example below:

```gradle
plugins {
    id "findbugs"
    id "java"
}

repositories {
    jcenter()
}

dependencies {
    compile "org.slf4j:slf4j-api:1.7.12"

    findbugsPlugins "jp.skypencil.findbugs.slf4j:bug-pattern:1.2.4@jar"
}
```

# Change log

See [CHANGELOG.md](CHANGELOG.md) for detail.

# Copyright and license

Copyright 2012-2017 Kengo TODA

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
