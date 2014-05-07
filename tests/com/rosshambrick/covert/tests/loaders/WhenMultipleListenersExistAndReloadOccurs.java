package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.Command;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.messages.AllCitiesQuery;
import com.rosshambrick.covert.tests.mocks.MockDependencyInjector;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WhenMultipleListenersExistAndReloadOccurs {
    private int mFirstLoad;
    private int mSecondLoad;
    private LoadListener<AllCitiesQuery> mFirstListener;
    private LoadListener<AllCitiesQuery> mSecondListener;
    private MockExecutor mExecutor;

    @Before
    public void setup() {

        mExecutor = new MockExecutor();
        CovertAgent covertAgent = new CovertAgent(
                new MockDependencyInjector(),
                mExecutor,
                new MockUiThread());

        mFirstListener = new LoadListener<AllCitiesQuery>() {
            @Override
            public void loadComplete(AllCitiesQuery data) {
                mFirstLoad++;
            }
        };
        mSecondListener = new LoadListener<AllCitiesQuery>() {
            @Override
            public void loadComplete(AllCitiesQuery data) {
                mSecondLoad++;
            }
        };
        covertAgent.load(new AllCitiesQuery(), mFirstListener);
        covertAgent.load(new AllCitiesQuery(), mSecondListener);

        covertAgent.send(new Command() {
            @Override
            protected void execute() {
                reload(new AllCitiesQuery());
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
