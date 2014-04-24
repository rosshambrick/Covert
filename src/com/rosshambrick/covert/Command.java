package com.rosshambrick.covert;

public abstract class Command extends CovertMessage {
    protected abstract void execute();

    public final void executeInternal() {
        if (isCanceled()) {
            //TODO: return CommandCanceled event?
            return;
        }
        execute();
    }

    //TODO: figure out if we want to encourage sending more commands or not
    protected void send(Command command) {
        mCovert.send(command);
    }

    public boolean isComplete() {
        return mComplete;
    }
}
