package com.rosshambrick.covert;

public interface CommandListener<T> {
    void commandComplete(T command);
}
