package com.rosshambrick.covert.tests.mocks;

import java.util.concurrent.Executor;

public class MockExecutor implements Executor {
    private int mCommandsSentCount;

    @Override
    public void execute(Runnable command) {
        mCommandsSentCount++;
        command.run();
    }

    public int getMessagesSentCount() {
        return mCommandsSentCount;
    }
}
