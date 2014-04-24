package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.mocks.FailingQuery;
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
        CovertAgent commandProcessor = new CovertAgent(null, mExecutor, new MockUiThread());
        commandProcessor.load(new FailingQuery(), this);
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
