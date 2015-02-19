package com.github.disc99.injector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;

public class InjectorTest {

    @Test
    public void test() {
        Injector injector = new Injector();
        Controller controller = injector.getInstance(Controller.class);
        int actual = controller.index();
        assertThat(actual, is(10));
    }

    @Test
    public void testName() throws Exception {
        new ClassScanner().scan("").stream().forEach(System.out::println);
    }
}

class Controller {
    @Inject
    Service service;

    int index() {
        return service.exe();
    }
}

interface Service {
    int exe();
}

class CalcService implements Service {
    @Override
    public int exe() {
        return 10;
    }
}

interface Dao {
    int select();
}

class CountDao implements Dao {
    @Override
    public int select() {
        return 10;
    }
}