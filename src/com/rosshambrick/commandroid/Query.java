package com.rosshambrick.commandroid;

public abstract class Query<T> extends AsyncMessage {

    protected abstract T load();

    public final T loadInternal() {
        if (isCanceled()) {
            //TODO: post a CommandCanceled event?
            return null;
        }

        return load();
    }

}
