package com.github.disc99.injector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Injector {

    private InjectionMappring mappring;

    public Injector() {
        init(null);
    }

    public Injector(InjectionMappring mappring) {
        init(mappring);
    }

    private void init(InjectionMappring mappring) {
        // TODO Auto-generated method stub
        List<Class<?>> injectTargetClasses = findInjectClass();
        List<Class<?>> injectedClasses = findInjectedClass();

        for (Class<?> targetClass : injectTargetClasses) {
            if (mappring.isBinded(targetClass)) {
                continue;
            }
            Class<?> extractedClass = extractInjectionClasses(targetClass, injectedClasses);
            mappring.bind(targetClass, extractedClass);
        }
    }

    private Class<?> extractInjectionClasses(Class<?> targetClass, List<Class<?>> injectedClasses) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Class<?>> findInjectClass() {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Class<?>> findInjectedClass() {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T getInstance(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            injectFields(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InjectException("fail get instance.", e);
        }
    }

    private <T> void injectFields(T instance) throws InstantiationException, IllegalAccessException {
        final Optional<List<Field>> injectTargetFields = findTargetFields(instance);
        if (injectTargetFields.isPresent()) {
            for (Field field : injectTargetFields.get()) {
                Object injectedInstance = findInjectClass(field).newInstance();
                field.set(instance, injectedInstance);
                injectFields(injectedInstance);
            }
        }
    }

    private Class<?> findInjectClass(Field field) {
        // TODO Auto-generated method stub
        return null;
    }

    private <T> Optional<List<Field>> findTargetFields(T instance) {
        // TODO
        return null;
    }
}

class InjectionMappring {
    private Map<Class<?>, Class<?>> mapping = new HashMap<>();

    public InjectionMappring bind(Class<?> key, Class<?> value) {
        mapping.put(key, value);
        return this;
    }

    public boolean isBinded(Class<?> cls) {
        return mapping.containsKey(cls);
    }
}