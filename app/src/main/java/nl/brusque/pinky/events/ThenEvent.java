package nl.brusque.pinky.events;

public class ThenEvent implements IEvent {
    public final Object onFulfilled;
    public final Object onRejected;

    public ThenEvent(Object onFulfilled, Object onRejected) {
        this.onFulfilled = onFulfilled;
        this.onRejected = onRejected;
    }
}
