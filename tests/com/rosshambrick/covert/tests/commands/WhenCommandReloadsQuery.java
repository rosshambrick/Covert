package com.rosshambrick.covert.tests.commands;

import com.rosshambrick.covert.tests.messages.ReloadCommand;
import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CommandListener;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.mocks.*;
import com.rosshambrick.covert.tests.messages.AllCitiesQuery;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WhenCommandReloadsQuery implements LoadListener<AllCitiesQuery>,CommandListener<ReloadCommand> {
    private int mNumLoads;

    @Before
    public void setup() {

        CovertAgent covertAgent = new CovertAgent(
                new MockDependencyInjector(),
                new MockExecutor(),
                new MockUiThread());

        covertAgent.load(new AllCitiesQuery(), this);

        covertAgent.send(new ReloadCommand(), this);
    }


    @Override
    public void loadComplete(AllCitiesQuery data) {
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
