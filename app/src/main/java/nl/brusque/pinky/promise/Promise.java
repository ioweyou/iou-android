package nl.brusque.pinky.promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.brusque.pinky.IPromise;

public class Promise implements IPromise {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final List<IFulfillable> _onFulfilleds   = new ArrayList<>();
    private final List<IRejectable> _onRejecteds     = new ArrayList<>();
    private IPromise _nextPromise;

    public IPromise resolve(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        _promiseState.resolve(o);
        nextResolve();

        return this;
    }

    private void nextResolve() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1); // Needed?
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (IFulfillable fulfilled : _onFulfilleds) {
                    try {
                        Object result = fulfilled.fulfill(_promiseState.getResolvedWith());
                        if (result!=null) { // FIXME Generics / Void
                            _nextPromise.resolve(result);
                        }
                    } catch (Exception e) {
                        _nextPromise.reject(e);
                    }
                }
            }
        });
    }

    public IPromise reject(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        _promiseState.reject(o.toString());
        nextReject();

        return this;
    }

    private void nextReject() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1); // Needed?
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (IRejectable onRejected : _onRejecteds) {
                    try {
                        Object result = onRejected.reject(_promiseState.RejectedWith());
                        if (result!=null) { // FIXME Generics / Void
                            _nextPromise.reject(result);
                        }
                    } catch (Exception e) {
                        _nextPromise.reject(e);
                    }
                }
            }
        });
    }

    @Override
    public IPromise then() {
        return then(null, null);
    }

    public IPromise then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    public IPromise then(Object onFulfilled, Object onRejected) {
        boolean isFulfilledSupplied = onFulfilled!=null && onFulfilled instanceof IFulfillable;
        boolean isRejectedSupplied  = onRejected!=null && onRejected instanceof IRejectable;
        if (isFulfilledSupplied) {
            _onFulfilleds.add((IFulfillable)onFulfilled);
        }

        if (isRejectedSupplied) {
            _onRejecteds.add((IRejectable)onRejected);
        }

        if (_promiseState.isRejected()) {
            nextReject();
        } else if (_promiseState.isResolved()) {
            nextResolve();
        }

        _nextPromise  = new Promise();

        return _nextPromise;
    }
}
