# CHANGELOG

This changelog follows [Keep a Changelog v1.0.0](https://keepachangelog.com/en/1.0.0/).

## Unreleased

## 1.4.1 - 2018-06-17
### Changed

- Upgrade maven-javadoc-plugin, lombok and errorprone to avoid NullPointerException and ExceptionInInitializerError
- Support SpotBugs 3.1.5 that deletes several constant fields

## 1.4.0 - 2017-11-13
### Changed

- Upgrade findbugs-maven-plugin to spotbugs-maven-plugin
- Upgrade several dependencies

## 1.3.0 - 2017-06-15
### Added

- Start providing SonarQube plugin

### Changed

- Upgrade FindBugs to SpotBugs 3.1.0

## 1.2.4 - 2015-09-26
### Added

- Translate bug pattern message to Japanese (issue #41)

### Changed

- Improve message of each bug patterns, and create unit tests to check messages (issue #39)

## 1.2.3 - 2015-09-19
### Fixed

- Fix broken bugInstance message (issue #36)

## 1.2.2 - 2015-09-19
### Fixed

- SLF4J_SIGN_ONLY_FORMAT should work even if format is given as method parameter (issue #36)
- Fix `Can't get stack offset 0 from []` bug (issue #37)

## 1.2.1 - 2015-09-13
### Fixed

- Downgrade Java to 7, to support Java 7 users (issue #33)
- SLF4J_FORMAT_SHOULD_BE_CONST should trace caller, to stop warning if all callers use constant value (issue #35)

## 1.2.0 - 2015-07-19
### Fixed

- Disabled `SLF4J_MANUALLY_PROVIDED_MESSAGE` if method parameter has no `Throwable` instance (issue #31)
- Stop printing many WARNING in `ThrowableHandler` (issue #30)

## 1.1.0 and 1.1.1 - 2015-07-18
### Changed

- Support SLF4J version 1.7.12
- Upgrade Java to 8
- Upgrade Findbugs to 3.0.1
- Updated Maven plugins
- Updated Dependencies

## 1.0.11 - 2013-12-31
### Fixed

- Fixed a bug which throws IllegalArgumentException (issue #29)

## 1.0.10 - 2013-12-10
### Added

- Explain abstract in README.md (issue #28)
- Supported Lombok (please disable SLF4J_LOGGER_SHOULD_BE_NON_STATIC)

## 1.0.9 - 2013-10-15
### Fixed

- Fixed overlooking problem (issue #2 and #14)
- SonarQube plugin supports SLF4J_MANUALLY_PROVIDED_MESSAGE bug pattern

## 1.0.8 - 2013-10-14
### Fixed

- Fixed runtime exception in detector for SLF4J_PLACE_HOLDER_MISMATCH (issue #18)

## 1.0.7 - 2013-09-28
### Fixed

- Solved missing library in Maven central (issue #24)

### Added

- Improved log message to debug (issue #18)

## 1.0.6 - 2013-07-27
### Fixed

- Fixed bugs which depends on compiler
    - when use LDC_W to push String from constant pool
    - when use LDC to push class from constant pool

### Changed

- Switch version of SLF4J from 1.7.2 to 1.7.5
- Start depending guava-libraries 14.0.1

### Added
- SLF4J_MANUALLY_PROVIDED_MESSAGE bug pattern

## 1.0.5 - 2013-04-14
### Fixed

- Fixed GETFIELD/GETSTATIC bug (issue #16)

## 1.0.4 - 2013-03-25
### Fixed

- Fixed IllegalStateException bug (issue #17)
- Fixed NullPointerException bug (issue #19)

## 1.0.3 - 2013-03-08
### Fixed

- Fixed exception handling bug (issue #14)

## 1.0.2 - 2013-03-06
### Fixed

- Fixed bug around Marker handling (issue #15)
- Fixed bug about returned value of private/static method cannot be marked as throwable
- Started to build with SLF4J 1.6.6 on Travis CI

## 1.0.1 - 2013-02-28
### Fixed

- Fixed ClassNotFoundException problem (issue #12)

## 1.0 - 2013-02-21
### Added

- SLF4J_LOGGER_SHOULD_BE_NON_STATIC bug pattern
- Redesigned priority

## 0.4 - 2013-02-01
### Added

- SLF4J_SIGN_ONLY_FORMAT bug pattern

## 0.3 - 2012-11-28
### Added

- SLF4J_LOGGER_SHOULD_BE_FINAL bug pattern
- SLF4J_ILLEGAL_PASSED_CLASS bug pattern

## 0.2 - 2012-11-12
### Fixed

- [bug fix] fixed findbugs.xml

### Added

- SLF4J_LOGGER_SHOULD_BE_PRIVATE bug pattern

## 0.1 - 2012-11-02
### Added

- SLF4J_PLACE_HOLDER_MISMATCH bug pattern
- SLF4J_FORMAT_SHOULD_BE_CONST bug pattern
- SLF4J_UNKNOWN_ARRAY bug pattern
