package com.steeplesoft.watkdemo;

import java.util.UUID;

public class MyModel {
    private UUID id = UUID.randomUUID();
    private String foo;
    private int bar;

    public UUID getId() {
        return id;
    }

    public MyModel setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getFoo() {
        return foo;
    }

    public MyModel setFoo(String foo) {
        this.foo = foo;
        return this;
    }

    public int getBar() {
        return bar;
    }

    public MyModel setBar(int bar) {
        this.bar = bar;
        return this;
    }
}
