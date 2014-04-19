package com.rosshambrick.commandroid;

public interface Bus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void publish(Object event);
}
