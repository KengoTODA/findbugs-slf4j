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

	private final JavaClass throwable;

	private boolean withoutFormat;

	private static final Set<String> TARGET_METHOD_NAMES = new HashSet<String>(
			Arrays.asList("trace", "debug", "info", "warn", "error"));

	// these methods do not use formatter
	private static final Set<String> SIGS_WITHOUT_FORMAT = new HashSet<String>(
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
		} else if (seen == AASTORE) {
			checkStoredInstance();
		}
	}

	private void checkStoredInstance() {
		Item storedValue = stack.getStackItem(0);
		Item arrayIndexItem = stack.getStackItem(1);
		Item targetArray = stack.getStackItem(2);
		Object arrayIndex = arrayIndexItem.getConstant();

		if (arrayIndex instanceof Number) {
			ArrayData data = (ArrayData) targetArray.getUserValue();
			Number index = (Number) arrayIndex;
			if (data != null && data.getSize() - 1 == index.intValue()) {
				data.setThrowableAtLast(IS_THROWABLE.equals(storedValue.getUserValue()));
			}
		}
	}

	@Override
	public void afterOpcode(int seen) {
		if (seen == ANEWARRAY) {
			tryToDetectArraySize(seen);
			return;	// `super.afterOpcode(seen)` has been called in #tryToDetectArraySize
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
		final int arraySize;
		try {
			Item arraySizeItem = stack.getStackItem(0);
			if (arraySizeItem != null && arraySizeItem.getConstant() instanceof Number) {
				// save array size as "user value"
				arraySize = ((Number) arraySizeItem.getConstant()).intValue();
			} else {
				// currently we ignore array which gets variable as array size
				arraySize = -1;
			}
		} finally {
			super.afterOpcode(seen);
		}

		final ArrayData arrayData = new ArrayData(arraySize);
		Item createdArray = stack.getStackItem(0);	// we can get created array after `super.afterOpcode(seen)` is called
		createdArray.setUserValue(arrayData);
	}

	private void checkLogger() {
		String signature = getSigConstantOperand();
		if (!"org/slf4j/Logger".equals(getClassConstantOperand())
				|| !TARGET_METHOD_NAMES.contains(getNameConstantOperand())) {
			return;
		}
		withoutFormat = SIGS_WITHOUT_FORMAT.contains(signature);

		String formatString = getFormatString(stack, signature);
		if (formatString == null || withoutFormat) {
			return;
		}

		int placeholderCount = countPlaceholder(formatString);
		int parameterCount;
		try {
			parameterCount = countParameter(stack, signature);
		} catch (IllegalStateException e) {
			// Using unknown array as parameter. In this case, we cannot check number of parameter.
			BugInstance bug = new BugInstance(this,
					"SLF4J_UNKNOWN_ARRAY", HIGH_PRIORITY)
					.addSourceLine(this).addClassAndMethod(this)
					.addCalledMethod(this);
			bugReporter.reportBug(bug);
			return;
		}

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
			ArrayData arrayData = (ArrayData) stack.getStackItem(0).getUserValue();
			if (arrayData == null || arrayData.getSize() < 0) {
				throw new IllegalStateException("no array initializer found");
			}
			int parameterCount = arrayData.getSize();
			if (arrayData.hasThrowableAtLast()) {
				--parameterCount;
			}
			return parameterCount;
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
