package nl.brusque.pinky;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.brusque.pinky.android.IRejectable;

public abstract class AbstractPromise<TResult extends IPromise> implements IPromise<AbstractPromise<TResult>> {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final List<IFulfillable> _onFulfilleds  = new ArrayList<>();
    private final List<IRejectable> _onRejecteds    = new ArrayList<>();
    private AbstractPromise<TResult> _nextPromise;

    public AbstractPromise<TResult> resolve(final Object o) {
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
                for (IFulfillable fulfilled : _onFulfilleds) {
                    try {
                        Object result = runFulfill(fulfilled, _promiseState.getResolvedWith());
                        if (result != null) { // FIXME Generics / Void
                            _nextPromise.resolve(result);
                        }
                    } catch (Exception e) {
                        _nextPromise.reject(e);
                    }
                }
            }
        });

        executor.shutdown();
    }

    public AbstractPromise<TResult> reject(final Object o) {
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
                for (IRejectable onRejected : _onRejecteds) {
                    try {
                        Object result = runReject(onRejected, _promiseState.RejectedWith());
                        if (result != null) { // FIXME Generics / Void
                            _nextPromise.reject(result);
                        }
                    } catch (Exception e) {
                        _nextPromise.reject(e);
                    }
                }
            }
        });

        executor.shutdown();
    }

    @Override
    public AbstractPromise<TResult> then() {
        return then(null, null);
    }

    public AbstractPromise<TResult> then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    public AbstractPromise<TResult> then(Object onFulfilled, Object onRejected) {
        if (isFulfillable(onFulfilled)) {
            _onFulfilleds.add((IFulfillable)onFulfilled);
        }

        if (isRejectable(onRejected)) {
            _onRejecteds.add((IRejectable)onRejected);
        }

        if (_promiseState.isRejected()) {
            nextReject();
        } else if (_promiseState.isResolved()) {
            nextResolve();
        }

        _nextPromise  = create();

        return _nextPromise;
    }
}
