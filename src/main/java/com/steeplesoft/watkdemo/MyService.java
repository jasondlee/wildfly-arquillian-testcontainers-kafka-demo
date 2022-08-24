package com.steeplesoft.watkdemo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@RequestScoped
public class MyService {

    @Inject
    @Channel("model")
    Emitter<MyModel> emitter;

    public MyModel sendModel(MyModel model) {
        emitter.send(model);

        return model;
    }
}
