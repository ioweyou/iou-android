package nl.brusque.pinky.promise;

import nl.brusque.pinky.AbstractPromise;
import nl.brusque.pinky.IFulfillable;
import nl.brusque.pinky.IPromise;
import nl.brusque.pinky.android.IRejectable;

public class Promise extends AbstractPromise<IPromise> {

    @Override
    public boolean isFulfillable(Object onFulfilled) {
        return onFulfilled != null && onFulfilled instanceof Fulfillable;
    }

    @Override
    public boolean isRejectable(Object onRejected) {
        return onRejected != null && onRejected instanceof Rejectable;
    }

    @Override
    public Object runFulfill(IFulfillable fulfillable, Object o) throws Exception {
        return fulfillable.fulfill(o);
    }

    @Override
    public Object runReject(IRejectable rejectable, Object o) throws Exception {
        return rejectable.reject(o);
    }

    @Override
    public AbstractPromise<IPromise> create() {
        return new Promise();
    }
}
