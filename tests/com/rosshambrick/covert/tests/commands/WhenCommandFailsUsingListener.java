package com.rosshambrick.covert.tests.commands;

import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
import com.rosshambrick.covert.Command;
import com.rosshambrick.covert.CommandListener;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenCommandFailsUsingListener implements CommandListener<Command> {
    private Command mCompleteCommand;

    @Before
    public void setup() {
        CovertAgent commandProcessor = new CovertAgent(null, new MockExecutor(), new MockUiThread());
        Command command = new Command() {
            @Override
            protected void execute() {
                throw new RuntimeException("Command error");
            }
        };
        commandProcessor.send(command, this);
    }

    @Override
    public void commandComplete(Command command) {
        mCompleteCommand = command;
    }

    @Test
    public void shouldPublishErrorEvent() {
        assertNotNull(mCompleteCommand.getError());

        Exception error = mCompleteCommand.getError();
        assertNotNull(error);

        assertTrue(error instanceof RuntimeException);

        String message = error.getMessage();
        assertEquals("Command error", message);
    }

}
