package com.rosshambrick.covert.tests.messages;

import com.rosshambrick.covert.Query;

import java.util.List;

public class FailingQuery extends Query<List<String>> {
    @Override
    protected List<String> load() {
        throw new RuntimeException("Load failed");
    }
}
