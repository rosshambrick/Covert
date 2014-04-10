package com.rosshambrick.commandroid;

public class CommandError {
    private final Command mCommand;
    private final Exception mException;

    public CommandError(Command command, Exception e) {
        mCommand = command;
        mException = e;
    }

    public Command getCommand() {
        return mCommand;
    }

    public Exception getException() {
        return mException;
    }
}
