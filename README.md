### Setting up WildFly
To download and install WildFly, copy and paste the following into your shell:
``` 
wget https://github.com/wildfly/wildfly/releases/download/26.1.1.Final/wildfly-26.1.1.Final.zip
unzip -q wildfly-26.1.1.Final.zip
wildfly-26.1.1.Final/bin/standalone.sh
wildfly-26.1.1.Final/bin/jboss-cli.sh -c << EOF
batch
/extension=org.wildfly.extension.microprofile.reactive-messaging-smallrye:add
/extension=org.wildfly.extension.microprofile.reactive-streams-operators-smallrye:add
/subsystem=microprofile-reactive-streams-operators-smallrye:add
/subsystem=microprofile-reactive-messaging-smallrye:add
run-batch
reload
EOF
```

### Starting Kafka
To download and install Kafka, perform the steps below. For more information on Kafka, see the
[Apache Kafka Quickstart](https://kafka.apache.org/quickstart).
```
$ wget https://dlcdn.apache.org/kafka/3.2.1/kafka_2.13-3.2.1.tgz
$ tar xf kafka_2.13-3.2.1.tgz
$ cd kafka_2.13-3.2.1.tgz
(In separate terminal windows/tabs)
$ bin/zookeeper-server-start.sh config/zookeeper.properties
$ bin/kafka-server-start.sh config/server.properties
(This next is optional)
$ bin/kafka-console-consumer.sh --topic ModelEvent --from-beginning --bootstrap-server localhost:9092
```

### Deploying the application

```
$ mvn clean install wildfly:deploy
```
