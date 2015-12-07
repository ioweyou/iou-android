package nl.brusque.pinky;

import nl.brusque.pinky.android.IRejectable;

public interface IPromise<TResult extends IPromise> extends IThenable<TResult> {
    boolean isFulfillable(Object onFulfilled);
    boolean isRejectable(Object onRejected);
    Object runFulfill(IFulfillable fulfillable, Object o) throws Exception;
    Object runReject(IRejectable rejectable, Object o) throws Exception;


    TResult create();
    TResult resolve(Object run);
    TResult reject(Object o);

}
