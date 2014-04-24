package com.rosshambrick.covert;

public interface Covert {
    //commands
    void send(Command command);
    <T extends Command> void send(final T command, CommandListener<T> listener);

    <T extends Query> void load(T query, LoadListener<T> listener);
    <T extends Query> void load(T query);
    <T extends Query> void reload(T query, LoadListener<T> listener);
    <T extends Query> void reload(T query);
    //TODO: decide of a reload(Class clazz) would be appropriate.  It would rerun last query instance


    long commandsRunning();
}
