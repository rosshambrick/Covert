package com.rosshambrick.commandroid;

public interface CommandProcessor {
    void send(Command command);
    void registerForEvents(Object o);
    void unregister(Object o);
}
