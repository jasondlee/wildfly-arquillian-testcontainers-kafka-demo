package com.steeplesoft.watkdemo;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Dependent
public class MessageService {
    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("model")
    @Outgoing("model-event")
    public Message<String> sendToKafka(MyModel model) {

        String data = jsonb.toJson(model);
        Message<String> m = Message.of(data);

        // Create Metadata containing the Kafka key
        OutgoingKafkaRecordMetadata<String> md = OutgoingKafkaRecordMetadata
                .<String>builder().withKey(model.getId().toString())
                .build();

        // The returned message will have the metadata added
        return KafkaMetadataUtil.writeOutgoingKafkaMetadata(m, md);
    }
}
