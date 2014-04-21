package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.commandroid.Command;

public class MockCommand extends Command {
    private boolean mCommandExecuted;
    private MockCommand mNextCommand;

    public MockCommand(MockCommand nextCommand) {
        mNextCommand = nextCommand;
    }

    public MockCommand() {

    }

    @Override
    protected void execute() {
        mCommandExecuted = true;
        if (mNextCommand != null) {
            send(mNextCommand);
        }
    }

    public boolean isCommandExecuted() {
        return mCommandExecuted;
    }
}
