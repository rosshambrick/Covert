package com.rosshambrick.commandroid;

import de.greenrobot.event.EventBus;

@SuppressWarnings("ALL")
public abstract class Command implements Runnable {
    private CommandProcessor mCommandProcessor;
    protected EventBus mEventBus;

    private boolean mIsCanceled;

    public void setCommandProcessor(CommandProcessor commandProcessor) {
        mCommandProcessor = commandProcessor;
    }

    public void setEventBus(EventBus eventBus) {
        mEventBus = eventBus;
    }

    protected abstract void execute();

    public final void run() {
        if (mIsCanceled) {
            //TODO: post a CommandCanceled event?
            return;
        }

        try {
            execute();
        } catch (Exception e) {
            //TODO: evaluate if a retry would be useful
            mEventBus.post(new CommandError(this, e));
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
