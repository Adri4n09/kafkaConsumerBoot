package com.example.dao;


import com.example.model.Book;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


public interface BookDao {

    Book getBookByISBN(String isbn);

    List<Book> getAllBooks();

    void addBook(Book book);

    void deleteBook(Book book);

    void updateBook(Book book);
}
