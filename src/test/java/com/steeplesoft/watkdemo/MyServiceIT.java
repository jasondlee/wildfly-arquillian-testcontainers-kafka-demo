package com.steeplesoft.watkdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MyServiceIT {

    @ArquillianResource
    private URL url;

    @Deployment
    public static Archive getDeployment() throws IOException {
        String config = Files.readString(Path.of("src/main/resources/META-INF/microprofile-config.properties"));
        config = config.replaceAll("localhost:9092", System.getProperty("kafka.server"));

        return ShrinkWrap.create(WebArchive.class, MyServiceIT.class.getName() + ".war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Warning: This breaks in EE 10
                .addAsResource(new StringAsset(config), "META-INF/microprofile-config.properties")
                .addPackages(true, MyService.class.getPackage().getName());
    }

    @Test
    @RunAsClient
    public void sendMessage() throws Exception {
        int count = 0;
        boolean found = false;

        sendRestRequest();

        KafkaConsumer<String, String> consumer = getConsumer();
        consumer.subscribe(Collections.singleton("ModelEvent"));

        while (!found && count < 10) {
            consumer.seekToBeginning(consumer.assignment());
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            count++;
            for (ConsumerRecord<String, String> r : records) {
                found = true;
                System.out.println("\n\n\n\n\n***** Message received: " + r.value() + "\n\n\n\n\n");
            }
        }
        assertTrue("Message not found in stream", found);
    }

    private void sendRestRequest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(url.toURI())
                .header("Accept", MediaType.APPLICATION_JSON)
                .header("Content-type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(
                        new ObjectMapper().writeValueAsString(new MyModel("foo", 49152))))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    private KafkaConsumer<String, String> getConsumer() {
        Map<String, Object> configurations = new HashMap<>();
        System.out.println(System.getProperty("kafka.server"));
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("kafka.server"));
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "watkdemo");
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new KafkaConsumer<>(configurations);
    }
}
