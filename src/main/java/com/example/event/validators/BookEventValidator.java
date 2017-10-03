package com.example.event.validators;

import com.example.event.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class BookEventValidator implements EventValidator {

    private ObjectMapper objectMapper;
    private Event event;

    public BookEventValidator(ObjectMapper objectMapper, Event event) {
        this.objectMapper = objectMapper;
        this.event = event;
    }

    @Override
    public boolean validateEvent(String e) {
        try {
            objectMapper.readValue(e, event.getClass());
            return true;
        } catch (Exception e1) {
        }
        return false;
    }
}
