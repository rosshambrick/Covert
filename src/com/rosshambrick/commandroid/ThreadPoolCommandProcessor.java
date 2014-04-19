package com.rosshambrick.commandroid;

import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("ALL")
public class ThreadPoolCommandProcessor implements CommandProcessor {
    public static final String TAG = "ThreadPoolCommandProcessor";

    private Executor mThreadPoolExecutor;
    private DependencyInjector mDependencyInjector;
    private Bus mBus;

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, Bus bus, Executor executor) {
        mBus = bus;
        mThreadPoolExecutor = executor;
        mDependencyInjector = dependencyInjector;
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, Bus bus, int threads) {
        this(dependencyInjector, bus, Executors.newFixedThreadPool(threads));
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, Bus bus) {
        this(dependencyInjector, bus, Executors.newSingleThreadExecutor());
    }

    @Override
    public void send(final Command command) {
        log("Sending: " + command.getClass().getSimpleName());

        command.setCommandProcessor(this);
        command.setBus(mBus);

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(command);
        }

        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                command.executeInternal();
            }
        });
    }

    @Override
    public void registerForEvents(Object o) {
        mBus.register(o);
    }

    @Override
    public void unregister(Object o) {
        mBus.unregister(o);
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
}
