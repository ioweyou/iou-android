package nl.brusque.pinky.helper;

import nl.brusque.pinky.promise.IRejectable;

public class RejectableSpy extends Spy implements IRejectable {
    @Override
    public Object reject(Object o) throws Exception {
        return call(o);
    }
}