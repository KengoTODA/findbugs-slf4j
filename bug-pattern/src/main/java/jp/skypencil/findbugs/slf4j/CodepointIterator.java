package jp.skypencil.findbugs.slf4j;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

/**
 * Iterator to iterate codepoint in provided {@link CharSequence}.
 * 
 * @see java.lang.CharSequence#codePoints() Replacement in Java8
 */
final class CodepointIterator implements Iterator<Integer> {
    @Nonnull
    private final CharSequence sequence;
    private int index;

    CodepointIterator(@Nonnull CharSequence sequence) {
        this.sequence = checkNotNull(sequence);
    }

    @Override
    public boolean hasNext() {
        return index < sequence.length();
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final int result;
        final char ch = sequence.charAt(index);
        if (Character.isHighSurrogate(ch)) {
            final char ch2 = sequence.charAt(index + 1);
            if (!Character.isLowSurrogate(ch2)) {
                throw new IllegalStateException("character at " + (index + 1) + " should be low surrogate");
            }
            result = (ch << 16) + ch2;
            index += 2;
        } else {
            result = ch;
            index += 1;
        }
        return Integer.valueOf(result);
    }

    @Override
    @Deprecated
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
