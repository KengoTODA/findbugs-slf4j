# CHANGE LOG
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
- Redesigned priority

## 1.0.1

- Fixed ClassNotFoundException problem (issue #12)

## 1.0.2

- Fixed bug around Marker handling (issue #15)
- Fixed bug about returned value of private/static method cannot be marked as throwable
- Started to build with SLF4J 1.6.6 on Travis CI

## 1.0.3

- Fixed exception handling bug (issue #14)

## 1.0.4

- Fixed IllegalStateException bug (issue #17)
- Fixed NullPointerException bug (issue #19)

## 1.0.5

- Fixed GETFIELD/GETSTATIC bug (issue #16)

## 1.0.6

- Fixed bugs which depends on compiler
    - when use LDC_W to push String from constant pool
    - when use LDC to push class from constant pool
- Switch version of SLF4J from 1.7.2 to 1.7.5
- Start depending guava-libraries 14.0.1
- SLF4J_MANUALLY_PROVIDED_MESSAGE bug pattern

## 1.0.7

- Solved missing library in Maven central (issue #24)
- Improved log message to debug (issue #18)

## 1.0.8

- Fixed runtime exception in detector for SLF4J_PLACE_HOLDER_MISMATCH (issue #18)

## 1.0.9

- Fixed overlooking problem (issue #2 and #14)
- SonarQube plugin supports SLF4J_MANUALLY_PROVIDED_MESSAGE bug pattern

## 1.0.10

- Explain abstract in README.md (issue #28)
- Supported Lombok (please disable SLF4J_LOGGER_SHOULD_BE_NON_STATIC)

## 1.0.11

- Fixed a bug which throws IllegalArgumentException (issue #29)

## 1.1.0 and 1.1.1

- Support SLF4J version 1.7.12
- Upgrade Java to 8
- Upgrade Findbugs to 3.0.1
- Updated Maven plugins
- Updated Dependencies

## 1.2.0

- Disabled `SLF4J_MANUALLY_PROVIDED_MESSAGE` if method parameter has no `Throwable` instance (issue #31)
- Stop printing many WARNING in `ThrowableHandler` (issue #30)

## 1.2.1

- Downgrade Java to 7, to support Java 7 users (issue #33)
- SLF4J_FORMAT_SHOULD_BE_CONST should trace caller, to stop warning if all callers use constant value (issue #35)

## 1.2.2

- SLF4J_SIGN_ONLY_FORMAT should work even if format is given as method parameter (issue #36)
- Fix `Can't get stack offset 0 from []` bug (issue #37)

## 1.2.3

- Fix broken bugInstance message (issue #36)

## 1.2.4

- Improve message of each bug patterns, and create unit tests to check messages (issue #39)
- Translate bug pattern message to Japanese (issue #41)

## 1.3.0

- Upgrade FindBugs to SpotBugs 3.1.0
- Start providing SonarQube plugin

## 1.4.0

- Upgrade findbugs-maven-plugin to spotbugs-maven-plugin
- Upgrade several dependencies

## 1.4.1

- Upgrade maven-javadoc-plugin, lombok and errorprone to avoid NullPointerException and ExceptionInInitializerError
- Support SpotBugs 3.1.5 that deletes several constant fields
