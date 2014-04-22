package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.covert.CoverAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.Query;
import com.rosshambrick.covert.tests.mocks.FailingQuery;
import com.rosshambrick.covert.tests.mocks.MockData;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenLoadingDataFails implements LoadListener<MockData> {
    private MockData mLoadedData;
    private MockExecutor mExecutor;
    private Query<MockData> mFailedQuery;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        CoverAgent commandProcessor = new CoverAgent(null, mExecutor);
        commandProcessor.load(new FailingQuery(), this);
    }

    @Override
    public void loadComplete(MockData data) {
        mLoadedData = data;
    }

    @Override
    public void loadFailed(Query<MockData> query) {
        mFailedQuery = query;
    }

    @Test
    public void shouldReturnFailedQuery() {
        assertNotNull(mFailedQuery);
        Exception error = mFailedQuery.getError();
        assertNotNull(error);
        assertTrue(error instanceof RuntimeException);
        assertEquals("Load failed", error.getMessage());
    }

    @Test
    public void shouldNotHaveCachedData() {
        assertNull(mLoadedData);
    }

}
