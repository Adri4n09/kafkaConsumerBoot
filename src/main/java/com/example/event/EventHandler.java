package com.example.event;

public interface EventHandler {

    public void onEvent(Event event);

    public boolean validEvent(Event event);
}
