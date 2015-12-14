package nl.brusque.pinky;

import android.util.Log;

import java.util.ArrayDeque;

import nl.brusque.pinky.android.IRejectable;
import nl.brusque.pinky.events.FireFulfillsEvent;
import nl.brusque.pinky.events.IEvent;
import nl.brusque.pinky.events.FireRejectsEvent;
import nl.brusque.pinky.events.RejectEvent;
import nl.brusque.pinky.events.FulfillEvent;
import nl.brusque.pinky.events.ThenEvent;
import nl.brusque.pinky.helper.FulfillableSpy;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;
import nl.brusque.pinky.promise.TypeError;
import nl.brusque.pinky.promise.TypeErrorException;

public abstract class AbstractPromise<TResult extends IPromise> implements IPromise<AbstractPromise<TResult>> {
    private final PromiseStateHandler _promiseState                 = new PromiseStateHandler();
    private final ArrayDeque<FulfillableWithPromise> _onFulfilleds  = new ArrayDeque<>();
    private final ArrayDeque<RejectableWithPromise> _onRejecteds    = new ArrayDeque<>();
    private final Thread _looper;

    private final ArrayDeque<IEvent> _eventQueue = new ArrayDeque<>();

    private class FulfillableWithPromise {
        private final AbstractPromise<TResult> _promise;
        private final IFulfillable _fulfillable;

        public FulfillableWithPromise(IFulfillable fulfillable, AbstractPromise<TResult> promise) {
            _promise = promise;
            _fulfillable = fulfillable;
        }

        public AbstractPromise<TResult> getPromise() {
            return _promise;
        }

        public IFulfillable getFulfillable() {
            return _fulfillable;
        }
    }

    private class RejectableWithPromise {
        private final AbstractPromise<TResult> _promise;
        private final IRejectable rejectable;

        public RejectableWithPromise(IRejectable rejectable, AbstractPromise<TResult> promise) {
            _promise = promise;
            this.rejectable = rejectable;
        }

        public AbstractPromise<TResult> getPromise() {
            return _promise;
        }

        public IRejectable getFulfillable() {
            return rejectable;
        }
    }


    private void queueEvent(IEvent event) {
        _eventQueue.add(event);
    }

    private synchronized void addFulfillable(IFulfillable fulfillable, AbstractPromise nextPromise) {
        _onFulfilleds.add(new FulfillableWithPromise(fulfillable, nextPromise));
    }
    private synchronized void addRejectable(IRejectable rejectable, AbstractPromise nextPromise) {
        _onRejecteds.add(new RejectableWithPromise(rejectable, nextPromise));
    }

    private synchronized IEvent dequeue() {
        if (_eventQueue.isEmpty()) {
            return null;
        }

        return _eventQueue.remove();
    }

    private synchronized void processNextEvent() {
        IEvent event = dequeue();
        if (event == null) {
            return;
        }

        if (event instanceof ThenEvent) {
            processEvent((ThenEvent)event);
        } else if (event instanceof FireFulfillsEvent) {
            processEvent((FireFulfillsEvent) event);
        } else if (event instanceof FireRejectsEvent) {
            processEvent((FireRejectsEvent)event);
        } else if (event instanceof FulfillEvent) {
            processEvent((FulfillEvent)event);
        } else if (event instanceof RejectEvent) {
            processEvent((RejectEvent)event);
        }
    }

    private void processEvent(FulfillEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (event.getValue() instanceof IPromise) {
            resolvePromise((IPromise)event.getValue());

            return;
        }

        _promiseState.fulfill(event.getValue());
        queueEvent(new FireFulfillsEvent());
    }

    private void processEvent(final RejectEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (event.getValue() instanceof IPromise) {
            resolvePromise((IPromise)event.getValue());

            return;
        }

        _promiseState.reject(event.getValue());
        queueEvent(new FireRejectsEvent());
    }

    private void resolvePromise(IPromise promise) {
        promise.then(new Fulfillable() {
            @Override
            public Object fulfill(Object o) throws Exception {
                _promiseState.fulfill(o);
                queueEvent(new FireFulfillsEvent());

                return o;
            }
        }, new Rejectable() {
            @Override
            public Object reject(Object o) throws Exception {
                _promiseState.reject(o);
                queueEvent(new FireRejectsEvent());

                return o;
            }
        });
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
            addFulfillable((IFulfillable)event.onFulfilled, event.nextPromise);
        }

        if (isRejectable) {
            addRejectable((IRejectable)event.onRejected, event.nextPromise);
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
        if (!_promiseState.isPending()) {
            return this;
        }

        try {
            if (o!=null && o.equals(this)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.
            queueEvent(new RejectEvent(new TypeError()));
            return this;
        }

        queueEvent(new FulfillEvent(o));

        return this;
    }

    private void nextResolve() {
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        //executor.submit(new Runnable() {
        //    @Override
        //    public void run() {

            //    for (IFulfillable fulfilled : _onFulfilleds) {

                while (!_onFulfilleds.isEmpty()) {
                    FulfillableWithPromise fulfilled = _onFulfilleds.remove();

                    try {
                        Object result = runFulfill(fulfilled.getFulfillable(), _promiseState.getResolvedWith());
                        if (result == null) {
                            return;
                        }

                        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                        fulfilled.getPromise().resolve(result);
                    } catch (Exception e) {
                        // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                        fulfilled.getPromise().reject(e);
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

        try {
            if (o!=null && o.equals(this)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.
            queueEvent(new RejectEvent(new TypeError()));
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
                    RejectableWithPromise onRejected = _onRejecteds.remove();

                    try {
                        Object result = runReject(onRejected.rejectable, _promiseState.RejectedWith());
                        if (result == null) {
                            return;
                        }

                        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                        onRejected.getPromise().reject(result);
                    } catch (Exception e) {
                        // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                        onRejected.getPromise().reject(e);
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
        AbstractPromise<TResult> nextPromise = create();
        queueEvent(new ThenEvent<>(onFulfilled, onRejected, nextPromise));

        return nextPromise;
    }
}
