package com.example.event;

import com.example.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BookEventHandler implements EventHandler {

    @Autowired
    @Qualifier("couchbaseDao")
    private BookDao bookDao;

    @Override
    public void onEvent(Event event) {
        if (validEvent(event)) {
            BookEvent bookEvent = (BookEvent) event;
            if (bookEvent.getOperation() == OperationType.ADD) {
                bookDao.addBook(bookEvent.getBook());
            }
        }

    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public boolean validEvent(Event event) {
        return event instanceof BookEvent;
    }

}
