package com.example;

import com.example.dao.BookDao;
import com.example.dao.BookDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@PropertySource({"classpath:properties/database.properties",
        "classpath:properties/kafkaConsumer.properties"})
@EnableTransactionManagement
public class ConsumerConfiguration implements TransactionManagementConfigurer {

    @Value("${jdbc.driverClassName}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

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
    private String topicName;


    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope(value = "prototype")
    public ConsumerThread consumerThread() {
        return new ConsumerThread(topicName);
    }

    @Bean
    @Qualifier("consumerList")
    @Scope(value = "prototype")
    public List<ConsumerThread> consumerThreadList() {
        List<ConsumerThread> list = new ArrayList<>();
        list.add(consumerThread());
        list.add(consumerThread());
        list.add(consumerThread());
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
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setHibernateProperties(getHibernateProperties());
        sessionFactoryBean.setMappingResources("orm/Books.hbm.xml");
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Bean
    public BookDao bookDao() {
        BookDaoImpl bookDao = new BookDaoImpl();
        bookDao.setSessionFactory(sessionFactory().getObject());
        return bookDao;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }
}
