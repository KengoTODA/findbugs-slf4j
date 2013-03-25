package jp.skypencil.findbugs.slf4j;
import static org.apache.bcel.Repository.lookupClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.Type;

final class JavaType {
    private static final Set<Type> PRIMITIVE_TYPES = new HashSet<Type>();
    private final String name;  // "java.lang.String" etc.
    private final JavaClass javaClass;

    static {
        PRIMITIVE_TYPES.addAll(Arrays.asList(new Type[]{
                Type.BOOLEAN, Type.BYTE, Type.SHORT, Type.CHAR, Type.INT,
                Type.LONG, Type.FLOAT, Type.DOUBLE
        }));
    }

    static JavaType from(JavaClass clazz) {
        if (clazz == null) {
            return null;
        } else {
            return new JavaType(clazz);
        }
    }

    static JavaType from(Type type) {
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

    JavaClass toJavaClass() throws ClassNotFoundException {
        if (javaClass != null) {
            return javaClass;
        } else if (name.contains("[")){
            return null;
        } else {
            return lookupClass(name);
        }
    }
}