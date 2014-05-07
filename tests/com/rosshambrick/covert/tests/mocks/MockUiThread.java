package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.UiThread;

public class MockUiThread implements UiThread {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
