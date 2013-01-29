package jp.skypencil.findbugs.slf4j

import org.junit.Test

import static org.junit.Assert.*

class AlwaysFailTest {

    @Test
    public void alwaysFail() {
        fail "always fails"
    }
}