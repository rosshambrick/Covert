package com.rosshambrick.covert;

public abstract class Query<TData> extends CovertMessage {

    private TData mData;

    protected abstract TData load();

    public final void loadInternal() {
        if (isCanceled()) {
            return;
        }

        mData = load();
    }

    public TData getResult() {
        return mData;
    }

    protected <T> T load(Query<T> query){
        return mCovert.syncLoad(query);
    }
}
