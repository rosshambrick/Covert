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
    public <TCommand extends Command> void send(final TCommand command, CommandListener<TCommand> listener) {
        mCommandsRunning.incrementAndGet();
        command.setCovert(this);

        WeakReference<CommandListener<TCommand>> weakListener = null;
        if (listener != null) {
            weakListener = new WeakReference<CommandListener<TCommand>>(listener);
        }

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(command);
        }

        final WeakReference<CommandListener<TCommand>> finalWeakListener = weakListener;

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
    public <TData, TQuery extends Query<TData>> void load(final TQuery query, final LoadListener<TQuery> listener) {
        Class<? extends Query> clazz = query.getClass();

        register(clazz, listener);

        TQuery cachedData = (TQuery) mQueryCache.get(clazz);
        if (cachedData != null) {
            if (listener != null) {
                listener.loadComplete(cachedData);
            }
        } else {
            queueLoad(query);
        }
    }

    private <TData, TQuery extends Query<TData>> void register(Class<? extends Query> clazz, LoadListener<TQuery> listener) {
        if (listener != null) {
            if (!mLoadListenerMap.containsKey(clazz)) {
                mLoadListenerMap.put(clazz, new HashSet<WeakReference<LoadListener>>());
            }
            mLoadListenerMap.get(clazz).add(new WeakReference<LoadListener>(listener));
        }
    }

    @Override
    public <TData, TQuery extends Query<TData>> void load(TQuery query) {
        load(query, null);
    }

    @Override
    public <TData, TQuery extends Query<TData>> void reload(TQuery query, LoadListener<TQuery> listener) {
        register(query.getClass(), listener);
        queueLoad(query);
    }

    @Override
    public <TData, TQuery extends Query<TData>> void reload(TQuery query) {
        queueLoad(query);
    }

    private <TData, TQuery extends Query<TData>> void queueLoad(final TQuery query) {
        mCommandsRunning.incrementAndGet();

        query.setCovert(this);

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(query);
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                doLoad(query);
                mCommandsRunning.decrementAndGet();
                mUiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        HashSet<WeakReference<LoadListener>> weakListeners = mLoadListenerMap.get(query.getClass());
                        for (WeakReference<LoadListener> listener : weakListeners) {
                            if (listener.get() != null) {
                                listener.get().loadComplete(query);
                            } else {
                                weakListeners.remove(listener);
                            }
                        }
                    }
                });
            }
        });
    }

    private <TData, TQuery extends Query<TData>> void doLoad(final TQuery query) {
        try {
            query.loadInternal();
            mQueryCache.put(query.getClass(), query);
            query.setSuccess(true);
        } catch (Exception e) {
            query.setError(e);
            query.setSuccess(true);
        }
    }

    @Override
    public long commandsRunning() {
        return mCommandsRunning.longValue();
    }

    @Override
    public <TData, TQuery extends Query<TData>> TData syncLoad(TQuery query) {
        doLoad(query);
        return query.getResult();
    }

    private static class LiveHandler implements UiThread {
        private Handler mHandler = new Handler();

        @Override
        public void post(Runnable runnable) {
            mHandler.post(runnable);
        }
    }
}
