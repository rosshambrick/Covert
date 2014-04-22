package com.rosshambrick.covert;

public abstract class AsyncMessage {
    Covert mCovert;
    private boolean mIsCanceled;
    private Exception mError;
    private boolean mFinished;

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
}
