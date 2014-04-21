package com.rosshambrick.commandroid;

public interface LoadListener<T> {
    public void loadComplete(T data);
    void loadFailed(Query<T> query);
}
