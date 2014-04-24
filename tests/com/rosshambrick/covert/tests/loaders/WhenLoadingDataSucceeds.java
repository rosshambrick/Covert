package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.tests.mocks.CitiesQuery;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class WhenLoadingDataSucceeds implements LoadListener<CitiesQuery> {
    private CitiesQuery mCitiesQuery;
    private MockExecutor mExecutor;

    @Before
    public void setup() {
        mExecutor = new MockExecutor();
        CovertAgent commandProcessor = new CovertAgent(null, mExecutor, new MockUiThread());
        commandProcessor.load(new CitiesQuery(), this);
        commandProcessor.load(new CitiesQuery(), this);
        commandProcessor.load(new CitiesQuery());
        commandProcessor.reload(new CitiesQuery(), this);
    }

    @Override
    public void loadComplete(CitiesQuery query) {
        mCitiesQuery = query;
    }

    @Test
    public void shouldHaveCachedData() {
        assertNotNull(mCitiesQuery);
        List<String> cities = mCitiesQuery.getResult();
        assertEquals(3, cities.size());
        assertEquals("Atlanta", cities.get(0));
    }

    @Test
    public void shouldOnlyLoadTwice() {
        assertEquals(2, mExecutor.getMessagesSentCount());
    }

    @Test
    public void shouldNotReturnAFailedQuery() {
        assertNull(mCitiesQuery.getError());
    }
}
