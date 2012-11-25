package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Repository.lookupClass;

import java.util.Deque;
import java.util.LinkedList;

import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class IllegalPassedClassDetector extends OpcodeStackDetector {
    private final BugReporter bugReporter;

    public IllegalPassedClassDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void afterOpcode(int code) {
        if (code != INVOKEVIRTUAL
                || !"getClass".equals(getNameConstantOperand())
                || !"java/lang/Object".equals(getClassConstantOperand())) {
            if (code != LDC_W) {
                super.afterOpcode(code);
            } else {
                JavaClass storedClass;
                try {
                    String storedClassName = getClassConstantOperand();
                    storedClass = lookupClass(storedClassName);
                } catch (ClassNotFoundException e) {
                    throw new AssertionError(e);
                } finally {
                    super.afterOpcode(code);
                }
                Item returnedClass = getStack().getStackItem(0);
                returnedClass.setUserValue(storedClass);
            }
            return;
        }

        Item caller = getStack().getStackItem(0);
        JavaClass classOfCaller;
        try {
            classOfCaller = caller.getJavaClass();
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        } finally {
            super.afterOpcode(code);
        }
        Item returnedClass = getStack().getStackItem(0);
        returnedClass.setUserValue(classOfCaller);
    }

    @Override
    public void sawOpcode(int code) {
        if (code != INVOKESTATIC) {
            return;
        } else if (!"org/slf4j/LoggerFactory".equals(getClassConstantOperand())
                || !"getLogger".equals(getNameConstantOperand())
                || !"(Ljava/lang/Class;)Lorg/slf4j/Logger;".equals(getSigConstantOperand())) {
            return;
        }
        final Item passedClass = getStack().getStackItem(0);
        if (!(passedClass.getUserValue() instanceof JavaClass)) {
            return;
        }

        final String passedClassName = ((JavaClass) passedClass.getUserValue()).getClassName();
        String callerClassName = getDottedClassName();
        Deque<String> acceptableClasses = new LinkedList<String>();
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

        BugInstance bug = new BugInstance(this,
                "SLF4J_ILLEGAL_PASSED_CLASS", HIGH_PRIORITY)
                .addString(acceptableClasses.toString())
                .addSourceLine(this)
                .addClass(this);
        bugReporter.reportBug(bug);
    }
}
