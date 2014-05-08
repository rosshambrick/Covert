package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Command;

public class MockCommand2 extends Command {
    private MockCommand2 mNextCommand;

    public MockCommand2(MockCommand2 nextCommand) {
        mNextCommand = nextCommand;
    }

    public MockCommand2() {

    }

    @Override
    protected void execute() {
        if (mNextCommand != null) {
            send(mNextCommand);
        }
    }
}
