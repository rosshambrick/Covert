package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.messages.FailingQuery;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenLoadingDataFails implements LoadListener<FailingQuery> {
    private FailingQuery mFailingQuery;
    private MockExecutor mExecutor;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        CovertAgent covertAgent = new CovertAgent(null, mExecutor, new MockUiThread());
        covertAgent.load(new FailingQuery(), this);
    }

    @Override
    public void loadComplete(FailingQuery query) {
        mFailingQuery = query;
    }

    @Test
    public void shouldReturnFailedQuery() {
        assertNotNull(mFailingQuery.getError());
        Exception error = mFailingQuery.getError();
        assertNotNull(error);
        assertTrue(error instanceof RuntimeException);
        assertEquals("Load failed", error.getMessage());
    }

    @Test
    public void shouldNotHaveCachedData() {
        assertNull(mFailingQuery.getResult());
    }

}
