package com.rosshambrick.covert.tests.loaders;

import com.rosshambrick.covert.CovertAgent;
import com.rosshambrick.covert.LoadListener;
import com.rosshambrick.covert.Query;
import com.rosshambrick.covert.tests.mocks.MockExecutor;
import com.rosshambrick.covert.tests.mocks.MockUiThread;
import com.rosshambrick.covert.tests.messages.FirstCityQuery;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WhenChainingLoads implements LoadListener<Query<String>> {
    private Query<String> mData;

    @Before
    public void setup() {
        CovertAgent covertAgent = new CovertAgent(null, new MockExecutor(), new MockUiThread());
        covertAgent.load(new FirstCityQuery(), this);
    }

    @Override
    public void loadComplete(Query<String> data) {
        mData = data;
    }

    @Test
    public void shouldReturnCorrectResult() {
        assertEquals("Atlanta", mData.getResult());
    }

}

