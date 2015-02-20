package com.github.disc99.injector;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Injector {

    private Class<? extends Annotation> INJECT = javax.inject.Inject.class;
    private Class<? extends Annotation> NAMED = javax.inject.Named.class;

    private ClassDependencies dependencies = new ClassDependencies();
    private List<Class<?>> namedClasses;

    public Injector() {
        init();
    }

    public Injector(ClassDependencies dependencies) {
        this.dependencies = dependencies;
        init();
    }

    public <T> T getInstance(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            injectFields(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InjectException("Fail get instance.", e);
        }
    }

    private void init() {
        List<Class<?>> allClasses = new ClassScanner().scan("");
        this.namedClasses = allClasses.stream()
                .filter(clz -> clz.isAnnotationPresent(NAMED))
                .collect(toList());
    }

    private <T> void injectFields(T instance) throws InstantiationException, IllegalAccessException {
        final Optional<List<Field>> injectTargetFields = findTargetFields(instance);
        if (injectTargetFields.isPresent()) {
            for (Field field : injectTargetFields.get()) {
                Object injectedInstance = findInjectClass(field).newInstance();
                field.setAccessible(true);
                field.set(instance, injectedInstance);
                injectFields(injectedInstance);
            }
        }
    }

    private Class<?> findInjectClass(Field field) {
        final Class<?> injectClass = field.getType();
        if (dependencies.isBound(injectClass)) {
            return dependencies.get(injectClass);
        }

        List<Class<?>> foundClasses = namedClasses.stream()
                .filter(cls -> injectClass.isAssignableFrom(cls))
                .collect(toList());

        if (foundClasses.size() == 1) {
            dependencies.bind(injectClass, foundClasses.get(0));
            return foundClasses.get(0);
        } else if (foundClasses.isEmpty()) {
            throw new InjectException("Injection class not found: " + injectClass);
        } else {
            throw new InjectException("Injection class there is more than one.");
        }
    }

    private <T> Optional<List<Field>> findTargetFields(T instance) {
        Field[] fields = instance.getClass().getDeclaredFields();

        return Optional.of(Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(INJECT))
                .collect(toList()));
    }
}
