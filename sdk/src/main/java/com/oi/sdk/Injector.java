package com.oi.sdk;

import com.oi.sdk.exception.InjectionException;

import java.util.HashMap;
import java.util.Map;

public class Injector {

    private static final Map<Class<?>, Object> _INSTANCES = new HashMap<>();    // Q: Need thread-safe?

    public static <T> T get(Class<T> clazz) {
        try {
            return clazz.cast(_INSTANCES.get(clazz));
        } catch (Exception e) {
            throw new InjectionException(clazz.getCanonicalName() + " not injected");
        }
    }

    public static <T> void inject(Class<? extends T> clazz, T instance) {
        _INSTANCES.putIfAbsent(clazz, instance);
    }

    public static <T> void inject(T instance) {
        inject(instance.getClass(), instance);
    }
}
