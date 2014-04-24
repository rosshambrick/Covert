package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.covert.UiThread;

public class MockUiThread implements UiThread {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
