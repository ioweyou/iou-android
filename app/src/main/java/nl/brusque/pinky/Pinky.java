package nl.brusque.pinky;

import nl.brusque.pinky.promise.Promise;

public class Pinky {
    public static IPromise resolve(final Object o) {
        Promise p = new Promise() {
            @Override
            public Object run(Object v) {
                return v;
            }
        };

        p.resolve(o);

        return p;
    }
}
