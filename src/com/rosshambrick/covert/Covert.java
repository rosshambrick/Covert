package com.rosshambrick.covert;

public interface Covert {
    //commands
    void send(Command command);
    <T extends Command> void send(final T command, CommandListener<T> listener);

    <T extends Query> void load(T query, LoadListener<T> listener);
    <T extends Query> void load(T query);
    <T extends Query> void reload(T query, LoadListener<T> listener);

    long commandsRunning();
}
