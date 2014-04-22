package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Query;

public class SuccessfulQuery extends Query<MockData> {
    @Override
    protected MockData load() {
        return new MockData();
    }
}
