package com.devisv.rest;

import com.google.inject.Injector;

import java.lang.reflect.ParameterizedType;

public abstract class Routing<T> {

    private final Injector injector;

    private Class<T> controller;

    protected Routing(Injector injector) {
        this.injector = injector;
    }

    public abstract void bindRoutes();

    public T getController() {
        return injector.getInstance(getControllerFromGenericType());
    }

    private Class<T> getControllerFromGenericType() {
        if (controller == null) {
            controller = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return controller;
    }
}
