package com.rosshambrick.covert;

public abstract class CovertMessage {
    protected boolean mComplete;
    Covert mCovert;
    private boolean mIsCanceled;
    private Exception mError;
    private boolean mFinished;
    private boolean mSuccess;

    public void setCovert(Covert covert) {
        mCovert = covert;
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

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }
}
