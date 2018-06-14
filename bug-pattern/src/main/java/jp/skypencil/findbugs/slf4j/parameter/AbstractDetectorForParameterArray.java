package jp.skypencil.findbugs.slf4j.parameter;

import static org.apache.bcel.Const.INVOKEINTERFACE;
import static org.apache.bcel.Const.INVOKESTATIC;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.internalAnnotations.StaticConstant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

/**
 * Basic detector to support detection about parameter array.
 *
 * @author Kengo TODA
 */
public abstract class AbstractDetectorForParameterArray extends OpcodeStackDetector {
  @StaticConstant
  private static final ImmutableSet<String> TARGET_METHOD_NAMES =
      ImmutableSet.of("trace", "debug", "info", "warn", "error");

  @StaticConstant
  // these methods do not use formatter
  private static final ImmutableSet<String> SIGS_WITHOUT_FORMAT =
      ImmutableSet.of(
          "(Ljava/lang/String;)V",
          "(Lorg/slf4j/Maker;Ljava/lang/String;)V",
          "(Ljava/lang/String;Ljava/lang/Throwable;)V",
          "(Lorg/slf4j/Maker;Ljava/lang/String;Ljava/lang/Throwable;)V");

  @StaticConstant
  private static final Pattern SIGNATURE_PATTERN = Pattern.compile("^\\((.*)\\).*$");

  private Table<Method, Integer, List<BugInstance>> potentialBugs;
  private Multimap<String, Integer> calledWithNonConstants;
  private Table<String, Integer, List<Object>> calledWithConstants;

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
  public void visitClassContext(ClassContext classContext) {
    potentialBugs = HashBasedTable.create();
    calledWithNonConstants = ArrayListMultimap.create();
    calledWithConstants = HashBasedTable.create();
    super.visitClassContext(classContext);
    validatePrivateMethodCall();
  }

  /**
   * Check all private method invocation, to detect {@code SLF4J_FORMAT_SHOULD_BE_CONST}.
   *
   * @see https://github.com/KengoTODA/findbugs-slf4j/issues/35
   */
  private void validatePrivateMethodCall() {
    for (Cell<Method, Integer, List<BugInstance>> cell : potentialBugs.cellSet()) {
      Method method = cell.getRowKey();
      String methodSignature = method.getName() + method.getSignature();
      if (calledWithNonConstants.containsEntry(methodSignature, cell.getColumnKey())) {
        for (BugInstance bug : cell.getValue()) {
          bugReporter.reportBug(bug);
        }
      }
    }
  }

  @Override
  public final void sawOpcode(int seen) {
    try {
      checkEvents(seen);
    } finally {
      arrayDataHandler.sawOpcode(getStack(), seen);
    }
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
    } else if (isMethodCall()) {
      if (Objects.equal(getClassDescriptor().getClassName(), getClassConstantOperand())) {
        // calling methods in the same class
        String methodSignature = getNameConstantOperand() + getSigConstantOperand();
        int arguments = Type.getArgumentTypes(getSigConstantOperand()).length;
        int shift = seen == INVOKESTATIC ? 0 : 1;
        for (int i = shift; i < arguments + shift; ++i) {
          Item item = getStack().getStackItem(i);
          if (item.getConstant() == null) {
            calledWithNonConstants.put(methodSignature, Integer.valueOf(i));
          } else {
            List<Object> list = calledWithConstants.get(methodSignature, Integer.valueOf(i));
            if (list == null) {
              list = Lists.newArrayList();
              calledWithConstants.put(methodSignature, Integer.valueOf(i), list);
            }
            list.add(item.getConstant());
          }
        }
      }
    }
  }

  @Nullable
  private ArrayData findArrayData(String signature) {
    int stackIndex = indexOf(signature, "[Ljava/lang/Object;");
    if (stackIndex == -1) {
      // generate ArrayData from items in stack
      String[] signatures = splitSignature(signature);
      int parameterCount = signatures.length - 1; // 1 means format is not a parameter
      if (signatures[0].equals("Lorg/slf4j/Marker;")) {
        --parameterCount;
      }
      ArrayData data = new ArrayData(parameterCount);
      for (int i = 0; i < data.getSize(); ++i) {
        Item item = getStack().getStackItem(i);
        arrayDataHandler.store(item, data, parameterCount - 1 - i);
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
    Item item = getStack().getStackItem(stackIndex);
    Object constant = item.getConstant();
    if (constant == null) {
      BugInstance bug =
          new BugInstance(this, "SLF4J_FORMAT_SHOULD_BE_CONST", HIGH_PRIORITY)
              .addSourceLine(this)
              .addClassAndMethod(this)
              .addCalledMethod(this);
      int argumentIndexOfLogFormat = getArgumentIndexOfLogFormat();
      if (argumentIndexOfLogFormat == -1 || !getMethod().isPrivate()) {
        bugReporter.reportBug(bug);
        return null;
      } else {
        // memorize bug instance, and validate it after we finish visiting class context.
        Method method = this.getMethod();
        List<BugInstance> storedList = potentialBugs.get(method, argumentIndexOfLogFormat);
        if (storedList == null) {
          storedList = Lists.newArrayList();
          potentialBugs.put(method, argumentIndexOfLogFormat, storedList);
        }
        storedList.add(bug);
        return null;
      }
    }

    if (SIGS_WITHOUT_FORMAT.contains(signature)) {
      return null;
    }
    return constant.toString();
  }

  protected final int getArgumentIndexOfLogFormat() {
    int stackIndex = indexOf(getSigConstantOperand(), "Ljava/lang/String;");
    Item item = getStack().getStackItem(stackIndex);
    /** 0 or greater value if item is `this` or argument. Otherwise -1. */
    int argumentIndex = item.getRegisterNumber();
    Method method = this.getMethod();
    if (argumentIndex != -1 && method.isStatic()) {
      argumentIndex++;
    }
    return argumentIndex;
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

  @Override
  @OverridingMethodsMustInvokeSuper
  public void afterOpcode(int seen) {
    OpcodeStack stack = getStack();
    ArrayData newUserValueToSet =
        arrayDataHandler.afterOpcode(stack, seen, getClassName(), getPC());

    super.afterOpcode(seen);

    if (newUserValueToSet != null) {
      Item createdArray = stack.getStackItem(0);
      createdArray.setUserValue(newUserValueToSet);
    }
  }

  protected final BugReporter getBugReporter() {
    return bugReporter;
  }

  protected final ThrowableHandler getThrowableHandler() {
    return throwableHandler;
  }

  protected final boolean isCalledWithNonConstants(Method method, Integer argumentIndex) {
    String methodSignature = method.getName() + method.getSignature();
    return calledWithNonConstants.containsEntry(methodSignature, argumentIndex);
  }

  @CheckForNull
  protected final List<Object> getConstantsToCall(Method method, Integer argumentIndex) {
    String methodSignature = method.getName() + method.getSignature();
    return calledWithConstants.get(methodSignature, argumentIndex);
  }

  protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {}
}
