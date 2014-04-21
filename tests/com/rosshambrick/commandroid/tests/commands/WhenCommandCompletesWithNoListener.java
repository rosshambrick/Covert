package com.rosshambrick.commandroid.tests.commands;

import com.rosshambrick.commandroid.Command;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.MockDependencyInjector;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
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
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(
                mDependencyInjector,
                mExecutor
        );

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
