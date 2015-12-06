package nl.brusque.pinky.helper;

import nl.brusque.pinky.promise.IFulfillable;

public class FulfillableSpy extends Spy implements IFulfillable {
    @Override
    public Object fulfill(Object o) throws Exception {
        return call(o);
    }

}