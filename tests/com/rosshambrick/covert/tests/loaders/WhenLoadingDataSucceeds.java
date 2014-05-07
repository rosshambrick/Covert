package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.messages.AllCitiesQuery;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class WhenLoadingDataSucceeds implements LoadListener<AllCitiesQuery> {
    private AllCitiesQuery mAllCitiesQuery;
    private MockExecutor mExecutor;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        CovertAgent commandProcessor = new CovertAgent(null, mExecutor, new MockUiThread());
        commandProcessor.load(new AllCitiesQuery(), this);
        commandProcessor.load(new AllCitiesQuery(), this);
        commandProcessor.load(new AllCitiesQuery());
        commandProcessor.reload(new AllCitiesQuery(), this);
    }

    @Override
    public void loadComplete(AllCitiesQuery query) {
        mAllCitiesQuery = query;
    }

    @Test
    public void shouldHaveCachedData() {
        assertNotNull(mAllCitiesQuery);
        List<String> cities = mAllCitiesQuery.getResult();
        assertEquals(3, cities.size());
        assertEquals("Atlanta", cities.get(0));
    }

    @Test
    public void shouldOnlyLoadTwice() {
        assertEquals(2, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldNotReturnAFailedQuery() {
        assertNull(mAllCitiesQuery.getError());
    }
}
