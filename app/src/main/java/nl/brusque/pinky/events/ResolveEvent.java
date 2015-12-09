package nl.brusque.pinky.events;

public class ResolveEvent implements IEvent {
    private final Object _o;

    public ResolveEvent(Object o) {
        _o = o;
    }

    public Object getValue() {
        return _o;
    }
}
