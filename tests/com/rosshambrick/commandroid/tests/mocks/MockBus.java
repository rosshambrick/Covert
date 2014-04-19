package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.commandroid.Bus;

import java.util.ArrayList;
import java.util.List;

public class MockBus implements Bus {
    private List<Object> mEvents = new ArrayList<Object>();

    @Override
    public void register(Object subscriber) {
        //not used in tests
    }

    @Override
    public void unregister(Object subscriber) {
        //not used in tests
    }

    @Override
    public void publish(Object event) {
        mEvents.add(event);
    }

    public Object getLastEvent() {
        int size = mEvents.size();
        if (size == 0) return null;
        return mEvents.get(size - 1);
    }
}
