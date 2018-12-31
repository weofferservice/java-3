package org.zcorp.java3;

public class LazySingleton {
    private static class LazyHolder {
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return LazyHolder.INSTANCE;
    }

    private LazySingleton() {
    }
}
