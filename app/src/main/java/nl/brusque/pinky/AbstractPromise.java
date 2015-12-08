package nl.brusque.pinky;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.brusque.pinky.android.IRejectable;
import nl.brusque.pinky.events.FulfillEvent;
import nl.brusque.pinky.events.IEvent;
import nl.brusque.pinky.events.RejectEvent;
import nl.brusque.pinky.events.ThenEvent;

public abstract class AbstractPromise<TResult extends IPromise> implements IPromise<AbstractPromise<TResult>> {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final List<IFulfillable> _onFulfilleds  = new ArrayList<>();
    private final List<IRejectable> _onRejecteds    = new ArrayList<>();
    private AbstractPromise<TResult> _nextPromise;

    private Queue<IEvent> _eventQueue = new ArrayDeque<>();

    private void pushEvent(IEvent event) {
        _eventQueue.add(event);
    }

    private void processNextEvent() {
        IEvent event = _eventQueue.remove();
        if (event instanceof ThenEvent) {
            processEvent((ThenEvent)event);
        } else if (event instanceof FulfillEvent) {
            processEvent((FulfillEvent)event);
        } else if (event instanceof RejectEvent) {
            processEvent((RejectEvent)event);
        }
    }

    private void processEvent(FulfillEvent event) {
        nextResolve();
    }

    private void processEvent(RejectEvent event) {
        nextReject();
    }

    private void processEvent(ThenEvent event) {
        if (_promiseState.isRejected()) {
            //nextReject();
            pushEvent(new RejectEvent());
        } else if (_promiseState.isResolved()) {
            pushEvent(new FulfillEvent());
            //nextResolve();
        }
    }

    public AbstractPromise() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    processNextEvent();
                }
            }
        });

        executor.shutdown();
    }

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
            _onRejecteds.add((IRejectable) onRejected);
        }

        pushEvent(new ThenEvent(onFulfilled, onRejected));

        if (_nextPromise == null) {
            _nextPromise = create();
        }

        return _nextPromise;
    }
}
