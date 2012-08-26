package jp.skypencil.findbugs.slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class WrongPlaceholderDetector extends OpcodeStackDetector {
	private static final Pattern PLACEHOLDER_PATTERN = Pattern
			.compile("(.?)(\\\\\\\\)*\\{\\}");

	private final BugReporter bugReporter;

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
	}

	@Override
	public void sawOpcode(int seen) {
		if (seen != INVOKEINTERFACE) {
			return;
		}

		String signature = getSigConstantOperand();
		if ("org/slf4j/Logger".equals(getClassConstantOperand())
				&& TARGET_METHOD_NAMES.contains(getNameConstantOperand())
				&& !NON_TARGET_SIGS.contains(signature)) {
			String formatString = getFormatString(stack, signature);
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
	}

	int countParameter(OpcodeStack stack, String methodSignature) {
		String[] signatures = splitSignature(methodSignature);
		int parameterCount = signatures.length - 1; // -1 means 'formatString'
													// is not parameter
		if (signatures[0].equals("Lorg/slf4j/Maker;")) {
			--parameterCount;
		}
		if (signatures[signatures.length - 1].equals("[Ljava/lang/Object;")) {
			Item array = stack.getStackItem(0);
			if (array.isArray()) {
				throw new UnsupportedOperationException("Object[] argument is not supported yet");
			}
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
			throw new UnsupportedOperationException("formatString should be constant");
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
