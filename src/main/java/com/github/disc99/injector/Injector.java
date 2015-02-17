package com.github.disc99.injector;

import java.util.List;

public class Injector {

    public <T> T getInstance(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();

            // inject fields
            injectFields(instance);

            // return instance
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InjectException("fail new instance.", e);
        }
    }

    private <T> void injectFields(T t) {
        // TODO
        List<?> classes = searchInjectClasses(t);

    }

    private <T> List<?> searchInjectClasses(T t) {
        // TODO search @Inject fields

        return null;
    }
}
