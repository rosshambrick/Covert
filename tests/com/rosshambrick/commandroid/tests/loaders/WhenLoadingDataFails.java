package com.rosshambrick.commandroid.tests.loaders;

import com.rosshambrick.commandroid.LoadListener;
import com.rosshambrick.commandroid.Query;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.FailingQuery;
import com.rosshambrick.commandroid.tests.mocks.MockData;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
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
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(null, mExecutor);
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
