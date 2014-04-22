package com.rosshambrick.covert;

//TODO: maybe a listener w/out error?
public interface CommandListener<T> {
    void commandComplete(T command);
    void commandFailed(T command);
}
