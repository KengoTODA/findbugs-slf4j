package jp.skypencil.findbugs.slf4j;

import static org.apache.bcel.Repository.lookupClass;

import com.google.common.collect.ImmutableSet;
import edu.umd.cs.findbugs.internalAnnotations.SlashedClassName;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.Type;

public final class JavaType {
  private static final ImmutableSet<BasicType> PRIMITIVE_TYPES;
  private static final JavaClass THROWABLE;
  private final String name; // "java.lang.String" etc.
  private final JavaClass javaClass;

  static {
    PRIMITIVE_TYPES =
        ImmutableSet.of(
            Type.BOOLEAN,
            Type.BYTE,
            Type.SHORT,
            Type.CHAR,
            Type.INT,
            Type.LONG,
            Type.FLOAT,
            Type.DOUBLE);
    try {
      THROWABLE = Repository.lookupClass(Throwable.class);
    } catch (ClassNotFoundException e) {
      throw new AssertionError(e);
    }
  }

  public static JavaType from(@SlashedClassName String className) {
    JavaClass javaClass;
    try {
      javaClass = Repository.lookupClass(className);
      return from(javaClass);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  public static JavaType from(JavaClass clazz) {
    if (clazz == null) {
      return null;
    } else {
      return new JavaType(clazz);
    }
  }

  public static JavaType from(Type type) {
    if (type == null || PRIMITIVE_TYPES.contains(type)) {
      return null;
    } else {
      return new JavaType(type);
    }
  }

  private JavaType(Type type) {
    this.name = type.toString();
    this.javaClass = null;
  }

  private JavaType(JavaClass clazz) {
    this.name = clazz.getClassName();
    this.javaClass = clazz;
  }

  @Override
  public String toString() {
    return name;
  }

  public JavaClass toJavaClass() throws ClassNotFoundException {
    if (javaClass != null) {
      return javaClass;
    } else if (name.contains("[")) {
      return null;
    } else {
      return lookupClass(name);
    }
  }

  public boolean isThrowable() {
    try {
      JavaClass javaClass = toJavaClass();
      if (javaClass == null) {
        return false;
      }
      return javaClass.instanceOf(THROWABLE);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
