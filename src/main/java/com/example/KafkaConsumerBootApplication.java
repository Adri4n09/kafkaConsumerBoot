package com.example;

import com.example.configs.ConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class KafkaConsumerBootApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(KafkaConsumerBootApplication.class, args);
		ApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
		MessageConsumer messageConsumer = (MessageConsumer) context.getBean("messageConsumer");
		messageConsumer.recieveMessage();
	}
}
