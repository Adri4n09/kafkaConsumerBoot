package com.example;

import com.example.event.Event;
import com.example.event.handlers.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Properties;


public class ConsumerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerThread.class);

    private String topicName;
    private KafkaConsumer<String, String> kafkaConsumer;

    private EventHandler eventHandler;
    private Event event;

    @Autowired
    private Properties properties;

    @Autowired
    private ObjectMapper objectMapper;

    public ConsumerThread(String topicName, EventHandler eventHandler, Event event) {
        this.topicName = topicName;
        this.eventHandler = eventHandler;
        this.event = event;
    }

    @Override
    public void run() {
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topicName));
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    if (eventHandler.validEvent(record.value())) {
                        logger.info("thread: " + Thread.currentThread().getId() + ", " + record.value() + " - valid");
                        eventHandler.onEvent(objectMapper.readValue(record.value(), event.getClass()));
                    }
                    else {
                        logger.info("thread: " + Thread.currentThread().getId() + ", " + record.value() + " - invalid");
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            logger.warn("Exception caught " + ex.getMessage());
        }finally{
            kafkaConsumer.close();
            logger.info("After closing KafkaConsumer");
        }
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }
}
