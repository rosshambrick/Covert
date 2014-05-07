package com.rosshambrick.covert.tests.messages;

import com.rosshambrick.covert.Query;

import java.util.List;

public class FirstCityQuery extends Query<String> {
    @Override
    protected String load() {
        List<String> cities = load(new AllCitiesQuery());
        return cities.get(0);
    }
}
