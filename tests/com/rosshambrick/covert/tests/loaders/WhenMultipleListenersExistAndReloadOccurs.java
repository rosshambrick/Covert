package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
import com.rosshambrick.covert.Command;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.mocks.CitiesQuery;
import com.rosshambrick.covert.tests.mocks.MockDependencyInjector;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WhenMultipleListenersExistAndReloadOccurs {
    private int mFirstLoad;
    private int mSecondLoad;
    private LoadListener<CitiesQuery> mFirstListener;
    private LoadListener<CitiesQuery> mSecondListener;
    private MockExecutor mExecutor;

    @Before
    public void setup() {

        mExecutor = new MockExecutor();
        CovertAgent covertAgent = new CovertAgent(
                new MockDependencyInjector(),
                mExecutor,
                new MockUiThread());

        mFirstListener = new LoadListener<CitiesQuery>() {
            @Override
            public void loadComplete(CitiesQuery data) {
                mFirstLoad++;
            }
        };
        mSecondListener = new LoadListener<CitiesQuery>() {
            @Override
            public void loadComplete(CitiesQuery data) {
                mSecondLoad++;
            }
        };
        covertAgent.load(new CitiesQuery(), mFirstListener);
        covertAgent.load(new CitiesQuery(), mSecondListener);

        covertAgent.send(new Command() {
            @Override
            protected void execute() {
                reload(new CitiesQuery());
            }
        });
    }

    @Test
    public void shouldLoadFirst() {
        assertEquals(2, mFirstLoad);
    }

    @Test
    public void shouldLoadSecond() {
        assertEquals(2, mSecondLoad);
    }

    @Test
    public void shouldSendTwoLoadsAndOneCommand() {
        assertEquals(3, mExecutor.getMessagesSentCount());
    }
}
