package com.example.event.handlers;

import com.example.event.BookEvent;
import com.example.event.Event;
import com.example.event.OperationType;
import com.example.event.validators.EventValidator;
import com.example.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookEventHandler implements EventHandler {

    private BookService bookService;

    @Autowired
    private EventValidator eventValidator;

    @Override
    public void onEvent(Event event) {
        BookEvent bookEvent = (BookEvent) event;
        if (bookEvent.getOperation() == OperationType.ADD) {
            bookService.addBook(bookEvent.getBook());
        }
    }

    @Override
    public boolean validEvent(String event) {
        return eventValidator.validateEvent(event);
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
