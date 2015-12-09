package nl.brusque.pinky.events;

public class RejectEvent implements IEvent {
    private final Object _o;

    public RejectEvent(Object o) {
        _o = o;
    }

    public Object getValue() {
        return _o;
    }
}
