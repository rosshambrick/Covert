package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.commandroid.Query;

public class SuccessfulQuery extends Query<MockData> {
    @Override
    protected MockData load() {
        return new MockData();
    }
}
