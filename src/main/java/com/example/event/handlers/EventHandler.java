package com.example.event.handlers;

import com.example.event.Event;

public interface EventHandler {

    public void onEvent(Event event);

    public boolean validEvent(String event);
}
