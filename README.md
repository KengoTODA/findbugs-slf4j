# FindBugs bug pattern for SLF4J
This product helps you to verify your product which uses SLF4J. Both of SLF4J 1.6 and 1.7 are supported.

[![Build Status](https://secure.travis-ci.org/eller86/findbugs-slf4j.png)](http://travis-ci.org/eller86/findbugs-slf4j)

Check [PMD ruleset for SLF4J](https://github.com/eller86/ruleset-for-SLF4J) if you need more help.

# bug pattern

Currently this product provides you 2 patterns.

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

# history

## 0.1

- SLF4J_PLACE_HOLDER_MISMATCH bug pattern
- SLF4J_FORMAT_SHOULD_BE_CONST bug pattern

# copyright and license
Copyright 2012 Kengo TODA (eller86)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.