package com.example.event.handlers;

import com.example.dao.BookDao;
import com.example.event.BookEvent;
import com.example.event.Event;
import com.example.event.OperationType;
import com.example.event.validators.EventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BookEventHandler implements EventHandler {

    @Autowired
    @Qualifier("mysqlDao")
    private BookDao bookDao;

    @Autowired
    private EventValidator eventValidator;

    @Override
    public void onEvent(Event event) {
        BookEvent bookEvent = (BookEvent) event;
        if (bookEvent.getOperation() == OperationType.ADD) {
            bookDao.addBook(bookEvent.getBook());
        }
    }

    @Override
    public boolean validEvent(String event) {
        return eventValidator.validateEvent(event);
    }

}
