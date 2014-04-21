package com.rosshambrick.commandroid;

import java.util.UUID;

public interface CommandProcessor {
    //commands
    UUID send(Command command);
    UUID send(Command command, CommandListener listener);
    void retryListener(UUID commandId, CommandListener listener);

    <T> void load(Query<T> query, LoadListener<T> listener);
    <T> void load(Query<T> query);
    <T> void reload(Query<T> query, LoadListener<T> listener);


//    void registerForEvents(Object o);
//    void unregister(Object o);
}
