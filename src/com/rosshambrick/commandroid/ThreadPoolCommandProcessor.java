package com.rosshambrick.commandroid;

import android.util.Log;
import de.greenrobot.event.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("ALL")
public class ThreadPoolCommandProcessor implements CommandProcessor {
    public static final String TAG = "ThreadPoolCommandProcessor";

    private Executor mThreadPoolExecutor;
    private DependencyInjector mDependencyInjector;
    private EventBus mEventBus;

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, EventBus eventBus, Executor executor) {
        mEventBus = eventBus;
        mThreadPoolExecutor = executor;
        mDependencyInjector = dependencyInjector;
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, int threads) {
        this(dependencyInjector, EventBus.getDefault(), Executors.newFixedThreadPool(threads));
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, EventBus eventBus, int threads) {
        this(dependencyInjector, eventBus, Executors.newFixedThreadPool(threads));
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, EventBus eventBus) {
        this(dependencyInjector, eventBus, Executors.newSingleThreadExecutor());
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector) {
        this(dependencyInjector, EventBus.getDefault());
    }

    public ThreadPoolCommandProcessor() {
        this(null);
    }

    @Override
    public void send(final Command command) {
        log("Sending: " + command.getClass().getSimpleName());

        command.setCommandProcessor(this);
        command.setEventBus(mEventBus);

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
        mEventBus.registerSticky(o);
    }

    @Override
    public void unregister(Object o) {
        mEventBus.unregister(o);
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
}
