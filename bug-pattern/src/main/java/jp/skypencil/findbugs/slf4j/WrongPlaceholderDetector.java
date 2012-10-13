package jp.skypencil.findbugs.slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class WrongPlaceholderDetector extends OpcodeStackDetector {
	private static final String IS_THROWABLE = "IS_THROWABLE";

	private static final Pattern PLACEHOLDER_PATTERN = Pattern
			.compile("(.?)(\\\\\\\\)*\\{\\}");

	private final BugReporter bugReporter;

	private JavaClass throwable;

	private static final Set<String> TARGET_METHOD_NAMES = new HashSet<String>(
			Arrays.asList("trace", "debug", "info", "warn", "error"));

	// these methods do not use formatter
	private static final Set<String> NON_TARGET_SIGS = new HashSet<String>(
			Arrays.asList("(Ljava/lang/String;)V",
					"(Lorg/slf4j/Maker;Ljava/lang/String;)V",
					"(Ljava/lang/String;Ljava/lang/Throwable;)V",
					"(Lorg/slf4j/Maker;Ljava/lang/String;Ljava/lang/Throwable;)V"));

	public WrongPlaceholderDetector(BugReporter bugReporter) {
		this.bugReporter = bugReporter;
		try {
			this.throwable = Repository.lookupClass("java/lang/Throwable");
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public void sawOpcode(int seen) {
		if (seen == INVOKEINTERFACE) {
			checkLogger();
		}
	}

	@Override
	public void afterOpcode(int seen) {
		if (seen == ANEWARRAY) {
			tryToDetectArraySize(seen);
			return;
		}

		super.afterOpcode(seen);

		if (seen == NEW) {
			markThrowableInstance();
		}
	}

	private void markThrowableInstance() {
		try {
			JavaClass clazz = Repository.lookupClass(getClassConstantOperand());
			if (clazz.instanceOf(throwable)) {
				stack.getStackItem(0).setUserValue(IS_THROWABLE);
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("class not found", e);
		}
	}

	private void tryToDetectArraySize(int seen) {
		Number arraySize;
		try {
			Item arraySizeItem = stack.getStackItem(0);
			if (arraySizeItem != null && arraySizeItem.getConstant() instanceof Number) {
				// save array size as "user value"
				arraySize = (Number) arraySizeItem.getConstant();
			} else {
				// currently we ignore array which gets variable as array size
				arraySize = null;
			}
		} finally {
			super.afterOpcode(seen);
		}

		Item createdArray = stack.getStackItem(0);	// we can get created array after `super.afterOpcode(seen)` is called
		createdArray.setUserValue(arraySize);
	}

	private void checkLogger() {
		String signature = getSigConstantOperand();
		if (!"org/slf4j/Logger".equals(getClassConstantOperand())
				|| !TARGET_METHOD_NAMES.contains(getNameConstantOperand())
				|| NON_TARGET_SIGS.contains(signature)) {
			return;
		}

		String formatString = getFormatString(stack, signature);
		if (formatString == null) {
			return;
		}

		int placeholderCount = countPlaceholder(formatString);
		int parameterCount = countParameter(stack, signature);

		if (placeholderCount != parameterCount) {
			BugInstance bug = new BugInstance(this,
					"SLF4J_PLACE_HOLDER_MISMATCH", HIGH_PRIORITY)
					.addInt(placeholderCount).addInt(parameterCount)
					.addSourceLine(this).addClassAndMethod(this)
					.addCalledMethod(this);
			bugReporter.reportBug(bug);
		}
	}

	int countParameter(OpcodeStack stack, String methodSignature) {
		String[] signatures = splitSignature(methodSignature);
		if (signatures[signatures.length - 1].equals("[Ljava/lang/Object;")) {
			Number arraySize = (Number) stack.getStackItem(0).getUserValue();
			if (arraySize.intValue() < 0) {
				throw new IllegalStateException("no array initializer found");
			}
			// TODO -1 if array contains a Throwable at last
			return arraySize.intValue();
		}

		int parameterCount = signatures.length - 1; // -1 means 'formatString'
													// is not parameter
		if (signatures[0].equals("Lorg/slf4j/Maker;")) {
			--parameterCount;
		}
		Item lastItem = stack.getStackItem(0);
		if (IS_THROWABLE.equals(lastItem.getUserValue())) {
			--parameterCount;
		}
		return parameterCount;
	}

	int countPlaceholder(String format) {
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(format);
		int count = 0;
		while (matcher.find()) {
			if (!"\\".equals(matcher.group(1))) {
				++count;
			}
		}
		return count;
	}

	private String getFormatString(OpcodeStack stack, String signature) {
		// formatString is the first string in argument
		int stackIndex = indexOf(signature, "Ljava/lang/String;");
		Object constant = stack.getStackItem(stackIndex).getConstant();
		if (constant == null) {
			BugInstance bug = new BugInstance(this,
					"SLF4J_FORMAT_SHOULD_BE_CONST", HIGH_PRIORITY)
					.addSourceLine(this).addClassAndMethod(this)
					.addCalledMethod(this);
			bugReporter.reportBug(bug);
			return null;
		}
		return constant.toString();
	}

	int indexOf(String methodSignature, String targetType) {
		String[] arguments = splitSignature(methodSignature);
		int index = arguments.length;

		for (String type : arguments) {
			--index;
			if (type.equals(targetType)) {
				return index;
			}
		}
		return -1;
	}

	private static final Pattern SIGNATURE_PATTERN = Pattern
			.compile("^\\((.*)\\).*$");

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
}
