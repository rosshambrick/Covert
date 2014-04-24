package com.rosshambrick.covert.tests.commands;

import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CommandListener;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.mocks.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WhenCommandReloadsQuery implements LoadListener<CitiesQuery>,CommandListener<ReloadCommand> {
    private int mNumLoads;

    @Before
    public void setup() {

        CovertAgent covertAgent = new CovertAgent(
                new MockDependencyInjector(),
                new MockExecutor(),
                new MockUiThread());

        covertAgent.load(new CitiesQuery(), this);

        covertAgent.send(new ReloadCommand(), this);
    }


    @Override
    public void loadComplete(CitiesQuery data) {
        mNumLoads++;
    }

    @Override
    public void commandComplete(ReloadCommand command) {

    }

    @Test
    public void shouldLoadTwice() {
        assertEquals(2, mNumLoads);
    }
}
