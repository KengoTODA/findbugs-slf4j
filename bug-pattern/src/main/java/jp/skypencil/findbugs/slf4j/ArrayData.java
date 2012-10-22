package jp.skypencil.findbugs.slf4j;

class ArrayData {
	private final int size;
	private boolean hasThrowableAtLast;

	ArrayData(int size) {
		this.size = size;
	}

	int getSize() {
		return size;
	}

	void setThrowableAtLast(boolean hasThrowableAtLast) {
		this.hasThrowableAtLast = hasThrowableAtLast;
	}

	boolean hasThrowableAtLast() {
		return hasThrowableAtLast;
	}
}
