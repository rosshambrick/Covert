package com.rosshambrick.commandroid.tests;

import com.rosshambrick.commandroid.Command;
import com.rosshambrick.commandroid.CommandError;
import com.rosshambrick.commandroid.tests.mocks.MockBus;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenCommandThrowsException {
    private MockBus mBus;

    @Before
    public void setup() {
        Command command = new Command() {
            @Override
            protected void execute() {
                throw new RuntimeException("Command error");
            }
        };
        mBus = new MockBus();
        command.setBus(mBus);
        command.executeInternal();
    }

    @Test
    public void shouldPublishErrorEvent() {
        Object lastEvent = mBus.getLastEvent();

        assertNotNull(lastEvent);

        assertTrue(lastEvent instanceof CommandError);

        CommandError commandError = (CommandError)lastEvent;
        Exception exception = commandError.getException();
        assertTrue(exception instanceof RuntimeException);

        String message = exception.getMessage();
        assertEquals("Command error", message);
    }
}
