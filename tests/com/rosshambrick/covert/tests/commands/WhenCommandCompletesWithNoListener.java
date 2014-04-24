package com.rosshambrick.covert.tests.commands;

import com.rosshambrick.covert.Command;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.tests.mocks.MockDependencyInjector;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class WhenCommandCompletesWithNoListener {
    private Command mFirstCommand;
    private boolean mFirstCommandExecuted;
    private boolean mSecondCommandExecuted;
    private MockDependencyInjector mDependencyInjector;
    private Command mSecondCommand;
    private MockExecutor mExecutor;

    @Before
    public void setup() {
        mDependencyInjector = new MockDependencyInjector();

        mExecutor = new MockExecutor();
        CovertAgent commandProcessor = new CovertAgent(
                mDependencyInjector,
                mExecutor,
                null);

        mSecondCommand = new Command() {
            @Override
            protected void execute() {
                mSecondCommandExecuted = true;
            }
        };

        mFirstCommand = new Command() {
            @Override
            protected void execute() {
                mFirstCommandExecuted = true;
                send(mSecondCommand);
            }
        };

        commandProcessor.send(mFirstCommand);
    }

    @Test
    public void shouldSendTwoCommands() {
        assertEquals(2, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldExecuteFirstCommand() {
        assertTrue(mFirstCommandExecuted);
    }

    @Test
    public void shouldExecuteNextCommand() {
        assertTrue(mSecondCommandExecuted);
    }

    @Test
    public void shouldInjectDependencies() {
        assertEquals(mFirstCommand, mDependencyInjector.get(0));
        assertEquals(mSecondCommand, mDependencyInjector.get(1));
    }
}
