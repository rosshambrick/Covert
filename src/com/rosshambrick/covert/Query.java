package com.rosshambrick.covert;

public abstract class Query<TData> extends CovertMessage {

    private TData mData;

    protected abstract TData load();

    public final void loadInternal() {
        if (isCanceled()) {
            //TODO: post a CommandCanceled event?
            return;
        }

        TData data = load();
        setData(data);
    }

    public TData getResult() {
        return mData;
    }

    public void setData(TData data) {
        mData = data;
    }
}
