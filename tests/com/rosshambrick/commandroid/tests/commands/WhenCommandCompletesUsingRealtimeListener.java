package com.rosshambrick.commandroid.tests.commands;

import com.rosshambrick.commandroid.CommandListener;
import com.rosshambrick.commandroid.tests.mocks.MockCommand;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
import com.rosshambrick.commandroid.tests.mocks.MockDependencyInjector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenCommandCompletesUsingRealtimeListener implements CommandListener<MockCommand> {
    private MockDependencyInjector mDependencyInjector;
    private MockExecutor mExecutor;
    private MockCommand mFirstCommand;
    private MockCommand mSecondCommand;
    private MockCommand mSuccessCommand;
    private MockCommand mErrorCommand;

    @Before
    public void setup() {
        mDependencyInjector = new MockDependencyInjector();

        mExecutor = new MockExecutor();
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(
                mDependencyInjector,
                mExecutor
        );

        mSecondCommand = new MockCommand();
        mFirstCommand = new MockCommand(mSecondCommand);
        commandProcessor.send(mFirstCommand, this);
    }

    @Override
    public void commandComplete(MockCommand command) {
        mSuccessCommand = command;
    }

    @Override
    public void commandFailed(MockCommand command) {
        mErrorCommand = command;
    }


    @Test
    public void shouldSendTwoCommands() {
        assertEquals(2, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldExecuteFirstCommand() {
        assertTrue(mFirstCommand.isCommandExecuted());
    }

    @Test
    public void shouldExecuteNextCommand() {
        assertTrue(mSecondCommand.isCommandExecuted());
    }

    @Test
    public void shouldRunCommand() {
        assertTrue(mFirstCommand.isCommandExecuted());
    }

    @Test
    public void shouldReturnSuccessResult() {
        assertNotNull(mSuccessCommand);
    }

    @Test
    public void shouldNotReturnErrorResult() {
        assertNull(mErrorCommand);
    }

    @Test
    public void shouldInjectDependencies() {
        assertEquals(mFirstCommand, mDependencyInjector.get(0));
        assertEquals(mSecondCommand, mDependencyInjector.get(1));
    }

}
