package nl.brusque.pinky.events;

import nl.brusque.pinky.AbstractPromise;
import nl.brusque.pinky.IPromise;

public class ThenEvent<TResult extends IPromise> implements IEvent {
    public final Object onFulfilled;
    public final Object onRejected;
    public final AbstractPromise<TResult> nextPromise;

    public ThenEvent(Object onFulfilled, Object onRejected, AbstractPromise<TResult> nextPromise) {
        this.onFulfilled = onFulfilled;
        this.onRejected  = onRejected;
        this.nextPromise = nextPromise;
    }
}
