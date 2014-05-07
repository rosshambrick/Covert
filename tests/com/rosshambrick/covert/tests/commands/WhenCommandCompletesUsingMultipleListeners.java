package com.rosshambrick.covert.tests.commands;

import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CommandListener;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.UiThread;
import com.rosshambrick.covert.tests.mocks.MockCommand;
import com.rosshambrick.covert.tests.mocks.MockCommand2;
import com.rosshambrick.covert.tests.mocks.MockDependencyInjector;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenCommandCompletesUsingMultipleListeners {
    private MockDependencyInjector mDependencyInjector;
    private MockExecutor mExecutor;
    private MockCommand mFirstCommand;
    private MockCommand2 mSecondCommand;
    private MockCommand mFirstCompleteCommand;
    private MockCommand mFirstFaileCommand;
    private MockCommand2 mSecondCompleteCommand;
    private MockCommand2 mSecondFailedCommand;
    private UiThread mUiThread;

    @Before
    public void setup() {
        mDependencyInjector = new MockDependencyInjector();
        mUiThread = new MockUiThread();

        mExecutor = new MockExecutor();
        CovertAgent commandProcessor = new CovertAgent(
                mDependencyInjector,
                mExecutor,
                mUiThread);

        mSecondCommand = new MockCommand2();
        mFirstCommand = new MockCommand();
        commandProcessor.send(mFirstCommand, new FirstCommandListener());
        commandProcessor.send(mSecondCommand, new SecondCommandListener());
    }

    private class FirstCommandListener implements CommandListener<MockCommand> {
        @Override
        public void commandComplete(MockCommand command) {
            mFirstCompleteCommand = command;
        }

//        @Override
//        public void commandFailed(MockCommand command) {
//            mFirstFaileCommand = command;
//        }
    }


    private class SecondCommandListener implements CommandListener<MockCommand2> {
        @Override
        public void commandComplete(MockCommand2 command) {
            mSecondCompleteCommand = command;
        }

//        @Override
//        public void commandFailed(MockCommand2 command) {
//            mSecondFailedCommand = command;
//        }
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
        assertNotNull(mFirstCompleteCommand);
    }

    @Test
    public void shouldNotReturnErrorResult() {
        assertNull(mFirstFaileCommand);
    }

    @Test
    public void shouldReturnSecondCompleteCommand() {
        assertNotNull(mSecondCompleteCommand);
    }

    @Test
    public void shouldNotReturnSecondFaileCommand() {
        assertNull(mSecondFailedCommand);
    }

    @Test
    public void shouldInjectDependencies() {
        assertEquals(mFirstCommand, mDependencyInjector.get(0));
        assertEquals(mSecondCommand, mDependencyInjector.get(1));
    }
}
