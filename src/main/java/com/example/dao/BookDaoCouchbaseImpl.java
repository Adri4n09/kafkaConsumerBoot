package com.example.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.example.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class BookDaoCouchbaseImpl implements BookDao {

    private static final Logger logger = LoggerFactory.getLogger(BookDaoCouchbaseImpl.class);

    @Autowired
    private Bucket bucket;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Book getBookByISBN(String isbn) {
        try {
            return mapper.readValue(bucket.get(isbn).content().toString(), Book.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("Select * from books"));
        return result.allRows().stream().map(getN1qlQueryRowBookFunction()).collect(Collectors.toList());
    }

    private Function<N1qlQueryRow, Book> getN1qlQueryRowBookFunction() {
        return a -> {
            try {
                return mapper.readValue(a.value().get("books").toString(), Book.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    @Override
    public void addBook(Book book) {
        try {
            bucket.insert(JsonDocument.create(book.getIsbn(), JsonObject.fromJson(mapper.writeValueAsString(book))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (DocumentAlreadyExistsException e) {
            logger.info("exists");
        }
    }

    @Override
    public void deleteBook(Book book) {
        bucket.remove(book.getIsbn());
    }

    @Override
    public void updateBook(Book book) {
        try {
            bucket.upsert(JsonDocument.create(book.getIsbn(), JsonObject.fromJson(mapper.writeValueAsString(book))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
