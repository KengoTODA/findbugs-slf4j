package jp.skypencil.findbugs.slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray;
import jp.skypencil.findbugs.slf4j.parameter.ArrayData;
import jp.skypencil.findbugs.slf4j.parameter.ArrayDataHandler;
import jp.skypencil.findbugs.slf4j.parameter.ThrowableHandler;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.OpcodeStack.Item;

@CustomUserValue
public class WrongPlaceholderDetector extends AbstractDetectorForParameterArray {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern
            .compile("(.?)(\\\\\\\\)*\\{\\}");

    public WrongPlaceholderDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    public void sawOpcode(int seen, ThrowableHandler throwableHandler) {
    }

    @Override
    protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
        if (format == null) {
            return;
        }

        verifyFormat(format);

        String signature = getSigConstantOperand();
        int placeholderCount = countPlaceholder(format);
        int parameterCount;
        try {
            parameterCount = countParameter(stack, signature, getThrowableHandler());
        } catch (IllegalStateException e) {
            // Using unknown array as parameter. In this case, we cannot check number of parameter.
            BugInstance bug = new BugInstance(this,
                    "SLF4J_UNKNOWN_ARRAY", HIGH_PRIORITY)
                    .addSourceLine(this).addClassAndMethod(this)
                    .addCalledMethod(this);
            getBugReporter().reportBug(bug);
            return;
        }

        if (placeholderCount != parameterCount) {
            BugInstance bug = new BugInstance(this,
                    "SLF4J_PLACE_HOLDER_MISMATCH", HIGH_PRIORITY)
                    .addInt(placeholderCount).addInt(parameterCount)
                    .addSourceLine(this).addClassAndMethod(this)
                    .addCalledMethod(this);
            getBugReporter().reportBug(bug);
        }
    }

    private void verifyFormat(String formatString) {
        CodepointIterator iterator = new CodepointIterator(formatString);
        while (iterator.hasNext()) {
            if (Character.isLetter(iterator.next().intValue())) {
                // found non-sign character.
                return;
            }
        }

        BugInstance bug = new BugInstance(this,
                "SLF4J_SIGN_ONLY_FORMAT", NORMAL_PRIORITY)
                .addString(formatString)
                .addSourceLine(this).addClassAndMethod(this)
                .addCalledMethod(this);
        getBugReporter().reportBug(bug);
    }

    int countParameter(OpcodeStack stack, String methodSignature, ThrowableHandler throwableHandler) {
        // fix issue 14
        String[] signatures = splitSignature(methodSignature);
        if (Objects.equal(signatures[signatures.length - 1], "[Ljava/lang/Object;")) {
            ArrayData arrayData = (ArrayData) stack.getStackItem(0).getUserValue();
            if (arrayData == null || arrayData.getSize() < 0) {
                throw new IllegalStateException("no array initializer found");
            }
            int parameterCount = arrayData.getSize();
            if (arrayData.isMarked()) {
                --parameterCount;
            }
            return parameterCount;
        }

        int parameterCount = signatures.length - 1; // -1 means 'formatString' is not parameter
        if (Objects.equal(signatures[0], "Lorg/slf4j/Marker;")) {
            --parameterCount;
        }
        Item lastItem = stack.getStackItem(0);
        if (throwableHandler.checkThrowable(lastItem)) {
            --parameterCount;
        }
        return parameterCount;
    }

    int countPlaceholder(String format) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(format);
        int count = 0;
        while (matcher.find()) {
            if (!Objects.equal("\\", matcher.group(1))) {
                ++count;
            }
        }
        return count;
    }

    private static final Pattern SIGNATURE_PATTERN = Pattern.compile("^\\((.*)\\).*$");

    private String[] splitSignature(String methodSignature) {
        final Matcher matcher = SIGNATURE_PATTERN.matcher(methodSignature);
        if (matcher.find()) {
            String[] arguments = matcher.group(1).split(";");
            for (int i = 0; i < arguments.length; ++i) {
                arguments[i] = arguments[i] + ';';
            }
            return arguments;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected ArrayDataHandler.Strategy createArrayCheckStrategy() {
        return new ArrayDataHandler.Strategy() {
            @Override
            public void store(Item storedItem, ArrayData data, int index) {
                if (data != null && data.getSize() - 1 == index) {
                    data.mark(getThrowableHandler().checkThrowable(storedItem));
                }
            }
        };
    }
}
