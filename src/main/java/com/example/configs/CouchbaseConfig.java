package com.example.configs;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.example.dao.BookDao;
import com.example.dao.BookDaoCouchbaseImpl;
import com.example.services.BookService;
import com.example.services.BookServiceImpl;
import org.springframework.context.annotation.*;

@Configuration
@Profile("couchbase")
public class CouchbaseConfig {

    @Bean
    public Cluster cluster() {
        return CouchbaseCluster.create("localhost");
    }

    @Bean
    public Bucket getBooksBucket() {
        return cluster().openBucket("books");
    }

    @Bean
    @Primary
    public BookDaoCouchbaseImpl bookDaoCouchbase() {
        BookDaoCouchbaseImpl bookDaoCouchbase = new BookDaoCouchbaseImpl();
        bookDaoCouchbase.setBucket(getBooksBucket());
        return bookDaoCouchbase;
    }

    @Bean
    @Primary
    public BookService bookService() {
        BookServiceImpl bookService = new BookServiceImpl();
        bookService.setBookDao(bookDaoCouchbase());
        return bookService;
    }
}
