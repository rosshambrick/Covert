package com.rosshambrick.commandroid;

import java.util.UUID;

@SuppressWarnings("ALL")
public abstract class Command extends AsyncMessage {

    private UUID mId;

    protected abstract void execute();

    public final void executeInternal() {
        if (isCanceled()) {
            //TODO: post a CommandCanceled event?
            return;
        }

//        try {
            execute();
//        } catch (Exception e) {
            //TODO: evaluate if a retry would be useful
//            mBus.publish(new AsyncMessageError(this, e));
//        }
    }

    //TODO: figure out if we want to encourage sending more commands or not
    protected void send(Command command) {
        mCommandProcessor.send(command);
    }

    public void setId(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }
}
