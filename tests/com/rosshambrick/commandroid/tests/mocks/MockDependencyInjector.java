package com.rosshambrick.commandroid.tests.mocks;

import com.rosshambrick.commandroid.DependencyInjector;

import java.util.ArrayList;
import java.util.List;

public class MockDependencyInjector implements DependencyInjector {
    private List<Object> mInjected = new ArrayList<Object>();

    @Override
    public void inject(Object object) {
        mInjected.add(object);
    }

    public Object lastInjected() {
        int size = mInjected.size();
        if (size == 0) return null;
        return mInjected.get(size - 1);
    }

    public Object get(int index) {
        return mInjected.get(index);
    }
}
