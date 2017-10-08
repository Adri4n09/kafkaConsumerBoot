package com.example.configs;

import com.example.ConsumerThread;
import com.example.MessageConsumer;
import com.example.event.BookEvent;
import com.example.event.handlers.BookEventHandler;
import com.example.event.handlers.EventHandler;
import com.example.event.validators.BookEventValidator;
import com.example.event.validators.EventValidator;
import com.example.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@PropertySource("classpath:properties/kafkaConsumer.properties")
@Import({CouchbaseConfig.class, MySqlConfig.class})
public class ConsumerConfiguration {

    @Value("${kafka.server}")
    private String KAFKA_SERVER;
    @Value("${kafka.key.deserializer}")
    private String KEY_DESERIALIZER;
    @Value("${kafka.value.deserializer}")
    private String VALUE_DESERIALIZER;
    @Value("${kafka.groupId}")
    private String GROUP_ID;
    @Value("${kafka.client.id.config}")
    private String CLIENT_ID_CONFIG;
    @Value("${kafka.books.topic}")
    private String booksTopicName;

    @Autowired
    BookService bookService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope(value = "prototype")
    public ConsumerThread booksConsumerThread() {
        return new ConsumerThread(booksTopicName, bookEventHandler(), bookEvent());
    }

    @Bean
    public BookEvent bookEvent() {
        return new BookEvent();
    }

    @Bean
    @Qualifier("consumerList")
    @Scope(value = "prototype")
    public List<ConsumerThread> consumerThreadList() {
        List<ConsumerThread> list = new ArrayList<>();
        list.add(booksConsumerThread());
        list.add(booksConsumerThread());
        list.add(booksConsumerThread());
        return list;
    }

    @Bean
    @Scope(value = "prototype")
    public Properties properties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return properties;
    }

    @Bean
    public MessageConsumer messageConsumer() {
        return new MessageConsumer();
    }

    @Bean
    public EventValidator bookEventValidator() {
        return new BookEventValidator(objectMapper(), new BookEvent());
    }

    @Bean
    public EventHandler bookEventHandler() {
        BookEventHandler eventHandler = new BookEventHandler();
        eventHandler.setBookService(bookService);
        return eventHandler;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
