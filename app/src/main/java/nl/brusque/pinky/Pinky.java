package nl.brusque.pinky;

import nl.brusque.pinky.promise.Pipe;

public class Pinky {
    public static IPromise resolve(final Object o) {
        Pipe p = new Pipe() {
            @Override
            public Object run(Object v) {
                return v;
            }
        };

        p.resolve(o);

        return p;
    }
}
