package com.github.disc99.injector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pl.playbit.di.annotations.Inject;

public class InjectorTest {

    @Test
    public void test() {
        Injector injector = new Injector();
        Controller controller = injector.getInstance(Controller.class);
        int actual = controller.index();
        assertThat(actual, is(10));
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