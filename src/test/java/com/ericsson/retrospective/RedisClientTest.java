package com.ericsson.retrospective;

import com.ericsson.retrospective.redis.RedisClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RStream;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Map;

public class RedisClientTest {

    static RedissonClient client;
    static RedissonClient consumer;
    @BeforeAll
    public static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.INFO);
    }
    @BeforeAll
    static void init() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        client = Redisson.create(config);
        consumer = Redisson.create(config);
    }

    @AfterAll
    static void shutdown(){
        client.shutdown();
        consumer.shutdown();
    }


    @Test
    void atomicLongTest_valid(){
        RAtomicLong myLong = client.getAtomicLong("myLong");
        myLong.set(15);
        System.out.println(myLong.get());
        myLong.incrementAndGet();
        System.out.println(myLong.get());
    }

    @Test
    void topicTest_rStream_valid(){
        String topic = "BRO";
        //get the send stream
        RStream<String, String> stream = client.getStream(topic);

        // send msg
        stream.add("REDIS_KEY2", "Notification 2");
        stream.add("REDIS_KEY", "Notification 3");
        stream.add(StreamAddArgs.entry("REDIS_KEY3", "Notification 4"));

        // consume msg
        RStream<String, String> consumerStream = consumer.getStream(topic);

        // consumer group
        String consumerGroup = "groupName1";
        String consumerId = "consumer01";
//        consumerStream.createGroup(consumerGroup, StreamMessageId.ALL);

        Map<StreamMessageId, Map<String, String>> messages = consumerStream.readGroup(consumerGroup, consumerId);

        for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
            // get time timestamp
            StreamMessageId messageId = entry.getKey();
            System.out.println("The received msg key is" + messageId.toString());
            System.out.println("The received msg key is" + messageId.getId0());
            System.out.println("The received msg key is" + messageId.getId1());

            Map<String, String> msg = entry.getValue();
            System.out.println("The received msg is: " + msg);

            consumerStream.ack(topic, messageId);
        }

    }
}
