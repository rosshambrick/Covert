package com.rosshambrick.commandroid;

@SuppressWarnings("ALL")
public abstract class Command {
    private CommandProcessor mCommandProcessor;
    protected Bus mBus;

    private boolean mIsCanceled;

    public void setCommandProcessor(CommandProcessor commandProcessor) {
        mCommandProcessor = commandProcessor;
    }

    public void setBus(Bus bus) {
        mBus = bus;
    }

    protected abstract void execute();

    public final void executeInternal() {
        if (mIsCanceled) {
            //TODO: post a CommandCanceled event?
            return;
        }

        try {
            execute();
        } catch (Exception e) {
            //TODO: evaluate if a retry would be useful
            mBus.publish(new CommandError(this, e));
        }
    }

    public void cancel() {
        mIsCanceled = true;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    //TODO: figure out if we want to encourage sending more commands or not
    protected void send(Command command) {
        mCommandProcessor.send(command);
    }
}
