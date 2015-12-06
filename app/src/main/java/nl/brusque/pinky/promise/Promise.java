package nl.brusque.pinky.promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        try {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    for (IFulfillable fulfilled : _onFulfilleds) {
                        try {
                            Object result = fulfilled.fulfill(o);
                            if (result!=null) { // FIXME Generics / Void
                                _nextPromise.resolve(result);
                            }
                        } catch (Exception e) {
                            _nextPromise.reject(e);
                        }
                    }
                }
            }, 10, TimeUnit.MILLISECONDS).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return this;
    }

    public IPromise reject(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        _promiseState.reject(o.toString());
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        try {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    for (IRejectable onRejected : _onRejecteds) {
                        try {
                            Object result = onRejected.reject(o);
                            if (result!=null) { // FIXME Generics / Void
                                _nextPromise.reject(result);
                            }
                        } catch (Exception e) {
                            _nextPromise.reject(e);
                        }
                    }
                }
            }, 10, TimeUnit.MILLISECONDS).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return this;
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

        if (!isFulfilledSupplied && !isRejectedSupplied) {
            throw new Error("Either fulfilled or rejected required.");
        }

        _nextPromise  = new Promise();

        return _nextPromise;
    }
}
