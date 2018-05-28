package jp.skypencil.findbugs.slf4j;

import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.internalAnnotations.StaticConstant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray;
import jp.skypencil.findbugs.slf4j.parameter.ArrayData;
import jp.skypencil.findbugs.slf4j.parameter.ArrayDataHandler;
import jp.skypencil.findbugs.slf4j.parameter.ThrowableHandler;
import org.apache.bcel.classfile.Method;

@CustomUserValue
public class WrongPlaceholderDetector extends AbstractDetectorForParameterArray {
  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(.?)(\\\\\\\\)*\\{\\}");
  /** Called method, index of argument for log message, and expected placeholders */
  private Table<Method, Integer, List<PotentialPlaceHolderMismatch>> potentialPlaceHolderMismatch;
  /** Called method, index of argument for log message, and expected placeholders */
  private Table<Method, Integer, List<PotentialSignOnlyFormat>> potentialSignOnlyFormat;

  public WrongPlaceholderDetector(BugReporter bugReporter) {
    super(bugReporter);
  }

  @Override
  public void visitClassContext(ClassContext classContext) {
    potentialPlaceHolderMismatch = HashBasedTable.create();
    potentialSignOnlyFormat = HashBasedTable.create();
    super.visitClassContext(classContext);
    validatePrivateMethodCall();
  }

  /**
   * Check all private method invocation, to detect {@code SLF4J_PLACE_HOLDER_MISMATCH}.
   *
   * @see https://github.com/KengoTODA/findbugs-slf4j/issues/35
   */
  private void validatePrivateMethodCall() {
    for (Cell<Method, Integer, List<PotentialPlaceHolderMismatch>> cell :
        potentialPlaceHolderMismatch.cellSet()) {
      Method method = cell.getRowKey();
      Integer argumentIndex = cell.getColumnKey();
      List<Object> constants = getConstantsToCall(method, argumentIndex);
      if (constants == null) {
        continue;
      }
      for (PotentialPlaceHolderMismatch bug : cell.getValue()) {
        for (Object constant : constants) {
          int placeholders = countPlaceholder(constant.toString());
          if (placeholders != bug.getParameterCount()) {
            getBugReporter().reportBug(bug.getRawBug(placeholders));
          }
        }
      }
    }
    for (Cell<Method, Integer, List<PotentialSignOnlyFormat>> cell :
        potentialSignOnlyFormat.cellSet()) {
      Method method = cell.getRowKey();
      Integer argumentIndex = cell.getColumnKey();
      List<Object> constants = getConstantsToCall(method, argumentIndex);
      if (constants == null) {
        continue;
      }
      for (PotentialSignOnlyFormat bug : cell.getValue()) {
        for (Object constant : constants) {
          String format = constant.toString();
          if (!verifyFormat(format)) {
            getBugReporter().reportBug(bug.getRawBug(format));
          }
        }
      }
    }
  }

  @Override
  protected void onLog(@Nullable String format, @Nullable ArrayData arrayData) {
    String signature = getSigConstantOperand();
    int parameterCount;
    try {
      parameterCount = countParameter(stack, signature, getThrowableHandler());
    } catch (IllegalStateException e) {
      // Using unknown array as parameter. In this case, we cannot check number of parameter.
      BugInstance bug =
          new BugInstance(this, "SLF4J_UNKNOWN_ARRAY", HIGH_PRIORITY)
              .addSourceLine(this)
              .addClassAndMethod(this)
              .addCalledMethod(this);
      getBugReporter().reportBug(bug);
      return;
    }

    if (format == null) {
      int argumentIndexOfLogFormat = getArgumentIndexOfLogFormat();
      get(potentialPlaceHolderMismatch, getMethod(), argumentIndexOfLogFormat)
          .add(
              new PotentialPlaceHolderMismatch(
                  createPlaceHolderMismatchBugInstance(-1, parameterCount), parameterCount));
      get(potentialSignOnlyFormat, getMethod(), argumentIndexOfLogFormat)
          .add(new PotentialSignOnlyFormat(createSignOnlyFormatBugInstance(format)));
      return;
    }
    int placeholderCount = countPlaceholder(format);
    if (!verifyFormat(format)) {
      getBugReporter().reportBug(createSignOnlyFormatBugInstance(format));
    }

    if (placeholderCount != parameterCount) {
      BugInstance bug = createPlaceHolderMismatchBugInstance(placeholderCount, parameterCount);
      getBugReporter().reportBug(bug);
    }
  }

  private BugInstance createSignOnlyFormatBugInstance(@Nullable String formatString) {
    BugInstance bug =
        new BugInstance(this, "SLF4J_SIGN_ONLY_FORMAT", NORMAL_PRIORITY)
            .addSourceLine(this)
            .addClassAndMethod(this)
            .addCalledMethod(this);
    if (formatString != null) {
      bug.addString(formatString);
    }
    return bug;
  }

  private BugInstance createPlaceHolderMismatchBugInstance(
      int placeholderCount, int parameterCount) {
    BugInstance bug =
        new BugInstance(this, "SLF4J_PLACE_HOLDER_MISMATCH", HIGH_PRIORITY)
            .addSourceLine(this)
            .addClassAndMethod(this)
            .addCalledMethod(this)
            .addInt(parameterCount);
    if (placeholderCount != -1) {
      bug.addInt(placeholderCount);
    }
    return bug;
  }

  /** @return true if given formatString is valid */
  private boolean verifyFormat(@Nonnull String formatString) {
    CodepointIterator iterator = new CodepointIterator(formatString);
    while (iterator.hasNext()) {
      if (Character.isLetter(iterator.next().intValue())) {
        // found non-sign character.
        return true;
      }
    }
    return false;
  }

  int countParameter(OpcodeStack stack, String methodSignature, ThrowableHandler throwableHandler) {
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

  @StaticConstant
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

  private <R, C, E> List<E> get(Table<R, C, List<E>> table, R row, C column) {
    List<E> list = table.get(row, column);
    if (list == null) {
      list = Lists.newArrayList();
      table.put(row, column, list);
    }
    return list;
  }

  private static final class PotentialSignOnlyFormat {
    private final BugInstance bug;

    private PotentialSignOnlyFormat(BugInstance bug) {
      this.bug = bug;
    }

    private BugInstance getRawBug(String format) {
      return bug.addString(format);
    }
  }

  private static final class PotentialPlaceHolderMismatch {
    private final BugInstance bug;
    private final int parameterCount;

    private PotentialPlaceHolderMismatch(BugInstance bug, int parameterCount) {
      this.bug = bug;
      this.parameterCount = parameterCount;
    }

    private BugInstance getRawBug(int placeholders) {
      return bug.addInt(placeholders);
    }

    private final int getParameterCount() {
      return parameterCount;
    }
  }
}
