package com.example;

import com.example.dao.BookDao;
import com.example.event.BookEvent;
import com.example.event.OperationType;
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

    @Autowired
    private Properties properties;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private ObjectMapper objectMapper;

    public ConsumerThread(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void run() {
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topicName));
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("thread: " + Thread.currentThread().getId() + ", " + record.value());
                    if (validBookEvent(record.value())) {
                        BookEvent bookEvent = objectMapper.readValue(record.value(), BookEvent.class);
                        if (bookEvent.getOperation() == OperationType.ADD) {
                            bookDao.addBook(bookEvent.getBook());
                        }
                    }
                }
            }
        }catch(Exception ex){
            System.out.println("Exception caught " + ex.getMessage());
        }finally{
            kafkaConsumer.close();
            System.out.println("After closing KafkaConsumer");
        }
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    private boolean validBookEvent(String book) {
        if (book.contains("isbn") || book.contains("title")) {
            return true;
        }
        return false;
    }
}
