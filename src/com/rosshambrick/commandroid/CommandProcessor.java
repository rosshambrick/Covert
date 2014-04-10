package com.rosshambrick.commandroid;

public interface CommandProcessor {
    void send(Command command);
//    void send(Command command, Listener listener);

//    interface Listener {
//        void onComplete();
//        void onError();
//    }
}
