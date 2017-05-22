package com.example;

import com.example.event.Event;
import com.example.event.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Properties;


public class ConsumerThread implements Runnable {

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
                    if (record.value().contains("Event")) {
                        System.out.println("thread: " + Thread.currentThread().getId() + ", " + record.value());
                        eventHandler.onEvent(objectMapper.readValue(record.value(), event.getClass()));
                    }
                    else {
                        System.out.println("thread: " + Thread.currentThread().getId() + ", " + record.value());
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Exception caught " + ex.getMessage());
        }finally{
            kafkaConsumer.close();
            System.out.println("After closing KafkaConsumer");
        }
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }
}
