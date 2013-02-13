# FindBugs bug pattern for SLF4J
This product helps you to verify your product which uses SLF4J. Both of SLF4J 1.6 and 1.7 are supported. To use this plugin with Sonar, see [here](sonar-plugin/README.md).

[![Build Status](https://secure.travis-ci.org/eller86/findbugs-slf4j.png)](http://travis-ci.org/eller86/findbugs-slf4j)

# bug pattern

Currently this product provides 8 patterns.

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

# how to use with Maven

To use this product, please configure your findbugs-maven-plugin like below.

```xml
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.5.2</version>
        <configuration>
          <plugins>
            <plugin>
              <groupId>jp.skypencil.findbugs.slf4j</groupId>
              <artifactId>bug-pattern</artifactId>
              <version>0.4</version>
            </plugin>
          </plugins>
        </configuration>
      </plugin>
```


# history

## 0.1

- SLF4J_PLACE_HOLDER_MISMATCH bug pattern
- SLF4J_FORMAT_SHOULD_BE_CONST bug pattern
- SLF4J_UNKNOWN_ARRAY bug pattern

## 0.2

- [bug fix] fixed findbugs.xml
- SLF4J_LOGGER_SHOULD_BE_PRIVATE bug pattern

## 0.3

- SLF4J_LOGGER_SHOULD_BE_FINAL bug pattern
- SLF4J_ILLEGAL_PASSED_CLASS bug pattern

## 0.4

- SLF4J_SIGN_ONLY_FORMAT bug pattern

## 1.0

- SLF4J_LOGGER_SHOULD_BE_NON_STATIC bug pattern


# copyright and license
Copyright 2012-2013 Kengo TODA (eller86)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
