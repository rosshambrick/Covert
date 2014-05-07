package com.rosshambrick.covert;

public interface Covert {
    //commands
    void send(Command command);
    <TCommand extends Command> void send(final TCommand command, CommandListener<TCommand> listener);

    <TData, TQuery extends Query<TData>> void load(TQuery query, LoadListener<TQuery> listener);
    <TData, TQuery extends Query<TData>> void load(TQuery query);
    <TData, TQuery extends Query<TData>> void reload(TQuery query, LoadListener<TQuery> listener);
    <TData, TQuery extends Query<TData>> void reload(TQuery query);
    //TODO: decide if a reload(Class clazz) would be appropriate.  It would rerun last query instance


    long commandsRunning();

    <TData, TQuery extends Query<TData>> TData syncLoad(TQuery query);
}
