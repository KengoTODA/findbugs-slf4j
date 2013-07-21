package jp.skypencil.findbugs.slf4j.parameter;

public class ArrayData {
    private final int size;
    private boolean hasThrowableAtLast;

    ArrayData(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    void setThrowableAtLast(boolean hasThrowableAtLast) {
        this.hasThrowableAtLast = hasThrowableAtLast;
    }

    public boolean hasThrowableAtLast() {
        return hasThrowableAtLast;
    }
}
