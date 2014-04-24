package com.rosshambrick.covert.tests.mocks;

import com.rosshambrick.covert.Query;

import java.util.Arrays;
import java.util.List;

public class CitiesQuery extends Query<List<String>> {
    @Override
    protected List<String> load() {
        return Arrays.asList("Atlanta", "New York", "Chicago");
    }
}
