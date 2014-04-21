package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.commandroid.Query;

public class FailingQuery extends Query<MockData> {
    @Override
    protected MockData load() {
        throw new RuntimeException("Load failed");
    }
}
