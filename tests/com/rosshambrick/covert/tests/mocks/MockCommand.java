package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Command;

public class MockCommand extends Command {

    private MockCommand mNextCommand;

    public MockCommand(MockCommand nextCommand) {
        mNextCommand = nextCommand;
    }

    public MockCommand() {

    }

    @Override
    protected void execute() {
        if (mNextCommand != null) {
            send(mNextCommand);
        }
    }
}
