package com.example.event;

import com.example.model.Book;

public class BookEvent {

    private String idBookEvent;
    private OperationType operation;
    private Book book;

    public String getIdBookEvent() {
        return idBookEvent;
    }

    public void setIdBookEvent(String idBookEvent) {
        this.idBookEvent = idBookEvent;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
