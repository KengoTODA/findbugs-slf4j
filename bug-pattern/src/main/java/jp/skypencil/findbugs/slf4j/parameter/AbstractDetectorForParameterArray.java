package jp.skypencil.findbugs.slf4j.parameter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.bcel.classfile.Method;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * <p>Basic detector to support detection about parameter array.
 * @author Kengo TODA
 */
public abstract class AbstractDetectorForParameterArray extends OpcodeStackDetector {
    private static final ImmutableSet<String> TARGET_METHOD_NAMES = ImmutableSet.of(
            "trace", "debug", "info", "warn", "error");

    // these methods do not use formatter
    private static final ImmutableSet<String> SIGS_WITHOUT_FORMAT = ImmutableSet.of(
            "(Ljava/lang/String;)V",
            "(Lorg/slf4j/Maker;Ljava/lang/String;)V",
            "(Ljava/lang/String;Ljava/lang/Throwable;)V",
            "(Lorg/slf4j/Maker;Ljava/lang/String;Ljava/lang/Throwable;)V");

    private static final Pattern SIGNATURE_PATTERN = Pattern.compile("^\\((.*)\\).*$");

    private final BugReporter bugReporter;
    private final ThrowableHandler throwableHandler;
    private final ArrayDataHandler arrayDataHandler;

    public AbstractDetectorForParameterArray(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
        this.throwableHandler = new ThrowableHandler();
        this.arrayDataHandler = new ArrayDataHandler(createArrayCheckStrategy());
    }

    protected abstract ArrayDataHandler.Strategy createArrayCheckStrategy();

    @Override
    public final void sawOpcode(int seen) {
        throwableHandler.sawOpcode(this, seen);
        try {
            checkEvents(seen);
            sawOpcode(seen, throwableHandler);
        } finally {
            arrayDataHandler.sawOpcode(stack, seen);
        }
    }

    @Override
    public void visitMethod(Method method) {
        throwableHandler.visitMethod();
        super.visitMethod(method);
    }

    private void checkEvents(int seen) {
        if (seen == INVOKEINTERFACE) {
            if (!Objects.equal("org/slf4j/Logger", getClassConstantOperand())
                    || !TARGET_METHOD_NAMES.contains(getNameConstantOperand())) {
                return;
            }
            String signature = getSigConstantOperand();
            String formatString = findFormatString(signature);
            ArrayData arrayData = findArrayData(signature);
            onLog(formatString, arrayData);
        }
    }

    @Nullable
    private ArrayData findArrayData(String signature) {
        int stackIndex = indexOf(signature, "[Ljava/lang/Object;");
        if (stackIndex == -1) {
            // generate ArrayData from items in stack
            String[] signatures = splitSignature(signature);
            int parameterOffset = 0;
            int parameterCount = signatures.length - 1; // 1 means format is not a parameter
            if (signatures[0].equals("Lorg/slf4j/Marker;")) {
                --parameterCount;
                parameterOffset = 1;
            }
            ArrayData data = new ArrayData(parameterCount);
            for (int i = 0; i < data.getSize(); ++i) {
                Item item = getStack().getStackItem(i + parameterOffset);
                arrayDataHandler.store(item, data, i);
            }
            return data;
        } else {
            Object userValue = getStack().getStackItem(stackIndex).getUserValue();
            if (userValue instanceof ArrayData) {
                return (ArrayData) userValue;
            }
        }

        return null;
    }

    @Nullable
    private String findFormatString(String signature) {
        // formatString is the first string in argument
        int stackIndex = indexOf(signature, "Ljava/lang/String;");
        Object constant = getStack().getStackItem(stackIndex).getConstant();
        if (constant == null) {
            BugInstance bug = new BugInstance(this,
                    "SLF4J_FORMAT_SHOULD_BE_CONST", HIGH_PRIORITY)
                    .addSourceLine(this).addClassAndMethod(this)
                    .addCalledMethod(this);
            bugReporter.reportBug(bug);
            return null;
        }

        if (SIGS_WITHOUT_FORMAT.contains(signature)) {
            return null;
        }
        return constant.toString();
    }

    @VisibleForTesting
    static int indexOf(String methodSignature, String targetType) {
        String[] arguments = splitSignature(methodSignature);
        int index = arguments.length;

        for (String type : arguments) {
            --index;
            if (Objects.equal(type, targetType)) {
                return index;
            }
        }
        return -1;
    }

    private static String[] splitSignature(String methodSignature) {
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

    protected abstract void sawOpcode(int seen, ThrowableHandler throwableHandler);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void afterOpcode(int seen) {
        ArrayData newUserValueToSet = arrayDataHandler.afterOpcode(stack, seen, getClassName(), getPC());

        super.afterOpcode(seen);

        if (newUserValueToSet != null) {
            Item createdArray = stack.getStackItem(0);
            createdArray.setUserValue(newUserValueToSet);
        }
        throwableHandler.afterOpcode(this, seen);
    }

    protected final BugReporter getBugReporter() {
        return bugReporter;
    }

    protected final ThrowableHandler getThrowableHandler() {
        return throwableHandler;
    }

    protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
    }
}
