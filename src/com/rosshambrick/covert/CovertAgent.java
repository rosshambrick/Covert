package com.rosshambrick.covert;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("ALL")
public class CovertAgent implements Covert {
    public static final String TAG = "ThreadPoolCommandProcessor";

    private Executor mExecutor;
    private DependencyInjector mDependencyInjector;

    private Map<Class, Object> mQueryCache = new HashMap<Class, Object>();
    private UiThread mUiThread;
    private AtomicLong mCommandsRunning = new AtomicLong(0);
    private Map<Class<? extends Query>, HashSet<WeakReference<LoadListener>>> mLoadListenerMap = new HashMap<Class<? extends Query>, HashSet<WeakReference<LoadListener>>>();

    public CovertAgent(DependencyInjector dependencyInjector, Executor executor, UiThread uiThread) {
        mExecutor = executor;
        mDependencyInjector = dependencyInjector;
        mUiThread = uiThread;
    }

    public CovertAgent(DependencyInjector dependencyInjector, int threads) {
        this(dependencyInjector, Executors.newFixedThreadPool(threads), new LiveHandler());
    }

    public CovertAgent(Executor executor) {
        this(null, executor, null);
    }

    public CovertAgent(DependencyInjector dependencyInjector) {
        this(dependencyInjector, Executors.newSingleThreadExecutor(), null);
    }

    public CovertAgent() {
        this(Executors.newSingleThreadExecutor());
    }

    @Override
    public void send(final Command command) {
        send(command, null);
    }

    @Override
    public <T extends Command> void send(final T command, CommandListener<T> listener) {
        mCommandsRunning.incrementAndGet();
        command.setCovert(this);

        WeakReference<CommandListener<T>> weakListener = null;
        if (listener != null) {
            weakListener = new WeakReference<CommandListener<T>>(listener);
        }

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(command);
        }

        final WeakReference<CommandListener<T>> finalWeakListener = weakListener;

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.executeInternal();
                    command.setSuccess(true);
                } catch (Exception e) {
                    //TODO: add logging
                    command.setError(e);
                } finally {
                    command.setComplete(true);
                    mCommandsRunning.decrementAndGet();
                    if (finalWeakListener != null && finalWeakListener.get() != null) {
                        mUiThread.post(new Runnable() {
                            @Override
                            public void run() {
                                if (finalWeakListener.get() != null) {
                                    finalWeakListener.get().commandComplete(command);
                                }
                            }
                        });
                    }
                }
            }
        });

        return;
    }

    @Override
    public <T extends Query> void load(final T query, final LoadListener<T> listener) {
        Class<? extends Query> clazz = query.getClass();

        register(clazz, listener);

        T cachedData = (T) mQueryCache.get(clazz);
        if (cachedData != null) {
            if (listener != null) {
                listener.loadComplete(cachedData);
            }
        } else {
            doLoad(query);
        }
    }

    private <T extends Query> void register(Class<? extends Query> clazz, LoadListener<T> listener) {
        if (listener != null) {
            if (!mLoadListenerMap.containsKey(clazz)) {
                mLoadListenerMap.put(clazz, new HashSet<WeakReference<LoadListener>>());
            }
            mLoadListenerMap.get(clazz).add(new WeakReference<LoadListener>(listener));
        }
    }

    @Override
    public <T extends Query> void load(T query) {
        load(query, null);
    }

    @Override
    public <T extends Query> void reload(T query, LoadListener<T> listener) {
        register(query.getClass(), listener);
        doLoad(query);
    }

    @Override
    public <T extends Query> void reload(T query) {
        doLoad(query);
    }

    private <T extends Query> void doLoad(final T query) {
        mCommandsRunning.incrementAndGet();

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(query);
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    query.loadInternal();
                    mQueryCache.put(query.getClass(), query);
                    query.setSuccess(true);
                } catch (Exception e) {
                    query.setError(e);
                } finally {
                    mCommandsRunning.decrementAndGet();
                    mUiThread.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeakReference<LoadListener> listener : mLoadListenerMap.get(query.getClass())) {
                                if (listener.get() != null) {
                                    listener.get().loadComplete(query);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public long commandsRunning() {
        return mCommandsRunning.longValue();
    }

    private static class LiveHandler implements UiThread {
        private Handler mHandler = new Handler();

        @Override
        public void post(Runnable runnable) {
            mHandler.post(runnable);
        }
    }
}
