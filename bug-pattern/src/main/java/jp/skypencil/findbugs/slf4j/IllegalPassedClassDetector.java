package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Const.CONSTANT_Class;
import static org.apache.bcel.Const.INVOKESTATIC;
import static org.apache.bcel.Const.INVOKEVIRTUAL;
import static org.apache.bcel.Const.LDC;
import static org.apache.bcel.Const.LDC_W;
import static org.apache.bcel.Repository.lookupClass;

import java.util.Deque;
import java.util.LinkedList;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.generic.Type;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.internalAnnotations.SlashedClassName;

@CustomUserValue
public class IllegalPassedClassDetector extends OpcodeStackDetector {
  private final BugReporter bugReporter;

  public IllegalPassedClassDetector(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void afterOpcode(int code) {
    if (code == INVOKEVIRTUAL
        && Objects.equal("getClass", getNameConstantOperand())
        && Objects.equal("java/lang/Object", getClassConstantOperand())) {
      memorizeResultOfGetClassMethod(code);
    } else if (code == LDC || code == LDC_W) {
      memorizeResultOfClassLiteral(code);
    } else {
      super.afterOpcode(code);
    }
  }

  private void memorizeResultOfClassLiteral(int code) {
    Constant constant = getConstantRefOperand();
    if (constant.getTag() != CONSTANT_Class) {
      super.afterOpcode(code);
      return;
    }

    JavaType storedClass;
    @SlashedClassName
    String storedClassName = getClassConstantOperand();
    try {
      storedClass = findClass(storedClassName);
    } catch (ClassFormatException e) {
      AnalysisContext.reportMissingClass(DescriptorFactory.createClassDescriptor(storedClassName));
      storedClass = null;
    } finally {
      super.afterOpcode(code);
    }
    if (storedClass != null) {
      Item returnedClass = getStack().getStackItem(0);
      returnedClass.setUserValue(storedClass);
    }
  }

  private void memorizeResultOfGetClassMethod(int code) {
    Item caller = getStack().getStackItem(0);
    JavaType classOfCaller;
    try {
      classOfCaller = JavaType.from(caller.getJavaClass());
    } catch (ClassNotFoundException e) {
      throw new AssertionError(e);
    } finally {
      super.afterOpcode(code);
    }
    Item returnedClass = getStack().getStackItem(0);
    returnedClass.setUserValue(classOfCaller);
  }

  @VisibleForTesting
  JavaType findClass(@SlashedClassName String storedClassName) throws ClassFormatException {
    try {
      return JavaType.from(lookupClass(storedClassName));
    } catch (ClassNotFoundException e) {
      // it might be int[] or others
      Type type = Type.getType(storedClassName);
      return JavaType.from(type);
    }
  }

  @Override
  public void sawOpcode(int code) {
    if (code == INVOKESTATIC
        && Objects.equal("org/slf4j/LoggerFactory", getClassConstantOperand())
        && Objects.equal("getLogger", getNameConstantOperand())
        && Objects.equal("(Ljava/lang/Class;)Lorg/slf4j/Logger;", getSigConstantOperand())) {
      final Item passedItem = getStack().getStackItem(0);
      final Object userValue = passedItem.getUserValue();
      if (userValue instanceof JavaType) {
        verifyPassedClassToGetLoggerMethod((JavaType) userValue);
      }
    }
  }

  private void verifyPassedClassToGetLoggerMethod(JavaType passedClass) {
    final String passedClassName = passedClass.toString();
    final Deque<String> acceptableClasses = new LinkedList<String>();

    String callerClassName = getDottedClassName();
    while (!callerClassName.isEmpty()) {
      if (callerClassName.equals(passedClassName)) {
        return;
      }
      acceptableClasses.push(callerClassName);
      int index = callerClassName.lastIndexOf('$');
      if (index == -1) {
        break;
      }
      callerClassName = callerClassName.substring(0, index);
    }

    BugInstance bug =
        new BugInstance(this, "SLF4J_ILLEGAL_PASSED_CLASS", HIGH_PRIORITY)
            .addString(acceptableClasses.toString())
            .addSourceLine(this)
            .addClass(this);
    bugReporter.reportBug(bug);
  }
}
