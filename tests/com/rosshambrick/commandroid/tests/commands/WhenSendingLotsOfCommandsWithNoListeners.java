package com.rosshambrick.commandroid.tests.commands;

import com.rosshambrick.commandroid.CommandListener;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.MockCommand;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class WhenSendingLotsOfCommandsWithNoListeners implements CommandListener<MockCommand> {
    private MockExecutor mExecutor;
    private MockCommand mFailedCommand;
    private MockCommand mCompleteCommand;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(
                null,
                mExecutor
        );

        UUID id1 = commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());
        commandProcessor.send(new MockCommand());

        commandProcessor.retryListener(id1, this);
    }

    @Override
    public void commandComplete(MockCommand command) {
        mCompleteCommand = command;
    }

    @Override
    public void commandFailed(MockCommand command) {
        mFailedCommand = command;
    }

    @Test
    public void shouldSendElevenCommands() {
        assertEquals(11, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldNoLongerBeAbleToRetryFirstCommand() {
        assertNull(mCompleteCommand);
        assertNull(mFailedCommand);
    }
}
