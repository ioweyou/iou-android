package nl.brusque.pinky;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.brusque.pinky.android.IRejectable;
import nl.brusque.pinky.events.FireFulfillsEvent;
import nl.brusque.pinky.events.IEvent;
import nl.brusque.pinky.events.FireRejectsEvent;
import nl.brusque.pinky.events.RejectEvent;
import nl.brusque.pinky.events.ResolveEvent;
import nl.brusque.pinky.events.ThenEvent;

public abstract class AbstractPromise<TResult extends IPromise> implements IPromise<AbstractPromise<TResult>> {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final ArrayDeque<IFulfillable> _onFulfilleds  = new ArrayDeque<>();
    private final ArrayDeque<IRejectable> _onRejecteds    = new ArrayDeque<>();
    private AbstractPromise<TResult> _nextPromise;
    private final Thread _looper;

    private ArrayDeque<IEvent> _eventQueue = new ArrayDeque<>();

    private void queueEvent(IEvent event) {
        _eventQueue.add(event);
    }

    private synchronized void addFulfillable(IFulfillable fulfillable) {
        _onFulfilleds.add(fulfillable);
    }
    private synchronized void addRejectable(IRejectable rejectable) {
        _onRejecteds.add(rejectable);
    }

    private void processNextEvent() {
        if (_eventQueue.isEmpty()) {
            return;
        }

        IEvent event = _eventQueue.remove();
        if (event instanceof ThenEvent) {
            processEvent((ThenEvent)event);
        } else if (event instanceof FireFulfillsEvent) {
            processEvent((FireFulfillsEvent) event);
        } else if (event instanceof FireRejectsEvent) {
            processEvent((FireRejectsEvent)event);
        } else if (event instanceof ResolveEvent) {
            processEvent((ResolveEvent)event);
        } else if (event instanceof RejectEvent) {
            processEvent((RejectEvent)event);
        }
    }

    private void processEvent(ResolveEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        _promiseState.resolve(event.getValue());
        queueEvent(new FireFulfillsEvent());
    }

    private void processEvent(RejectEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        _promiseState.reject(event.getValue());
        queueEvent(new FireRejectsEvent());
    }

    private void processEvent(FireFulfillsEvent event) {
        nextResolve();
    }

    private void processEvent(FireRejectsEvent event) {
        nextReject();
    }

    private void processEvent(ThenEvent event) {
        boolean isFulfillable = isFulfillable(event.onFulfilled);
        boolean isRejectable  = isRejectable(event.onRejected);

        if (!isFulfillable || !isRejectable) {
            Log.w("PROCESS", String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
        }

        if (isFulfillable) {
            addFulfillable((IFulfillable)event.onFulfilled);
        }

        if (isRejectable) {
            addRejectable((IRejectable)event.onRejected);
        }

        if (_promiseState.isRejected()) {
            queueEvent(new FireRejectsEvent());
        } else if (_promiseState.isResolved()) {
            queueEvent(new FireFulfillsEvent());
        }
    }

    public AbstractPromise() {
        _looper = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    processNextEvent();
                }
            }
        });

        _looper.start();
    }

    public AbstractPromise<TResult> resolve(final Object o) {
        queueEvent(new ResolveEvent(o));

        return this;
    }

    private void nextResolve() {
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        //executor.submit(new Runnable() {
        //    @Override
        //    public void run() {

            //    for (IFulfillable fulfilled : _onFulfilleds) {

                while (!_onFulfilleds.isEmpty()) {
                    IFulfillable fulfilled = _onFulfilleds.remove();

                    try {
                        Object result = runFulfill(fulfilled, _promiseState.getResolvedWith());
                        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                        if (result != null) { // FIXME Generics / Void
                            _nextPromise.resolve(result);
                        }
                    } catch (Exception e) {
                        // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                        _nextPromise.reject(e);
                    }
                }
        //    }
        //});

        //executor.shutdown();
    }

    public AbstractPromise<TResult> reject(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        queueEvent(new RejectEvent(o));

        return this;
    }

    private void nextReject() {
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        //executor.submit(new Runnable() {
        //    @Override
        //    public void run() {
                //for (IRejectable onRejected : _onRejecteds) {

                while (!_onRejecteds.isEmpty()) {
                    IRejectable onRejected = _onRejecteds.remove();

                    try {
                        Object result = runReject(onRejected, _promiseState.RejectedWith());
                        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                        if (result != null) { // FIXME Generics / Void
                            _nextPromise.reject(result);
                        }
                    } catch (Exception e) {
                        // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                        _nextPromise.reject(e);
                    }
                }
        //    }
        //});

        //executor.shutdown();
    }

    @Override
    public AbstractPromise<TResult> then() {
        return then(null, null);
    }

    public AbstractPromise<TResult> then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    public AbstractPromise<TResult> then(Object onFulfilled, Object onRejected) {
        queueEvent(new ThenEvent(onFulfilled, onRejected));

        if (_nextPromise == null) {
            _nextPromise = create();
        }

        return _nextPromise;
    }
}
