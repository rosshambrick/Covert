package com.rosshambrick.commandroid.tests.loaders;

import com.rosshambrick.commandroid.Query;
import com.rosshambrick.commandroid.tests.mocks.SuccessfulQuery;
import com.rosshambrick.commandroid.LoadListener;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.MockData;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class WhenLoadingDataSucceeds implements LoadListener<MockData> {
    private MockData mLoadedData;
    private MockExecutor mExecutor;
    private Query<MockData> mFailedQuery;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(null, mExecutor);
        commandProcessor.load(new SuccessfulQuery(), this);
        commandProcessor.load(new SuccessfulQuery(), this);
        commandProcessor.load(new SuccessfulQuery());
        commandProcessor.reload(new SuccessfulQuery(), this);
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
    public void shouldHaveCachedData() {
        assertNotNull(mLoadedData);
        assertEquals(3, mLoadedData.CITIES.size());
        assertEquals("Atlanta", mLoadedData.CITIES.get(0));
    }

    @Test
    public void shouldOnlyLoadOnce() {
        assertEquals(2, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldNotReturnAFailedQuery() {
        assertNull(mFailedQuery);
    }
}
