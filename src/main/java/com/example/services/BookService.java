package com.example.services;

import com.example.model.Book;

import java.util.List;

public interface BookService {

    Book getBookByISBN(String isbn);

    List<Book> getAllBooks();

    void addBook(Book book);

    void deleteBook(Book book);

    void updateBook(Book book);
}
