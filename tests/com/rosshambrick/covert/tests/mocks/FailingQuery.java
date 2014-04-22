package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Query;

public class FailingQuery extends Query<MockData> {
    @Override
    protected MockData load() {
        throw new RuntimeException("Load failed");
    }
}
