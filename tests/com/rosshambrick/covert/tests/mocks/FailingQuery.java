package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Query;

public class FailingQuery extends Query<CitiesData> {
    @Override
    protected CitiesData load() {
        throw new RuntimeException("Load failed");
    }
}
