package com.steeplesoft.watkdemo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

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
