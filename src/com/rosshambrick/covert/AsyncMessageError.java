package com.rosshambrick.covert;

public class AsyncMessageError {
    private final AsyncMessage mAsyncMessage;
    private final Exception mException;

    public AsyncMessageError(AsyncMessage asyncMessage, Exception e) {
        mAsyncMessage = asyncMessage;
        mException = e;
    }

    public AsyncMessage getAsyncMessage() {
        return mAsyncMessage;
    }

    public Exception getException() {
        return mException;
    }
}
