package com.rosshambrick.commandroid;

public abstract class AsyncMessage {
    CommandProcessor mCommandProcessor;
    private boolean mIsCanceled;
    private Exception mError;
    private boolean mFinished;

    public void setCommandProcessor(CommandProcessor commandProcessor) {
        mCommandProcessor = commandProcessor;
    }

    public void cancel() {
        mIsCanceled = true;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    public void setError(Exception error) {
        mError = error;
    }

    public Exception getError() {
        return mError;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    public boolean isFinished() {
        return mFinished;
    }
}
