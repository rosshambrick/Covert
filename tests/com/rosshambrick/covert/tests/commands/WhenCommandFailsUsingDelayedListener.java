//package com.rosshambrick.covert.tests.commands;
//
//import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
//import com.rosshambrick.covert.Command;
//import com.rosshambrick.covert.CommandListener;
//import com.rosshambrick.covert.CovertAgent;
//import com.rosshambrick.covert.tests.mocks.MockExecutor;
//import org.junit.Before;
//import org.junit.Test;
//
//import static junit.framework.Assert.*;
//
//public class WhenCommandFailsUsingDelayedListener implements CommandListener<Command> {
//    private Command mFailedCommand;
//    private Command mCompleteCommand;
//
//    @Before
//    public void setup() {
//        CovertAgent commandProcessor = new CovertAgent(null, new MockExecutor(), new MockUiThread());
//        Command command = new Command() {
//            @Override
//            protected void execute() {
//                throw new RuntimeException("Command error");
//            }
//        };
//        long id = commandProcessor.send(command);
//        commandProcessor.resetListener(id, this);
//    }
//
//    @Override
//    public void commandComplete(Command command) {
//        mCompleteCommand = command;
//    }
//
////    @Override
////    public void commandFailed(Command command) {
////        mFailedCommand = command;
////    }
//
//    @Test
//    public void shouldReturnFailedCommand() {
//        assertFalse(mCompleteCommand.isSuccess());
//        assertNotNull(mCompleteCommand.getError());
//
//        Exception error = mCompleteCommand.getError();
//        assertNotNull(error);
//
//        assertTrue(error instanceof RuntimeException);
//
//        String message = error.getMessage();
//        assertEquals("Command error", message);
//    }
//
//}
