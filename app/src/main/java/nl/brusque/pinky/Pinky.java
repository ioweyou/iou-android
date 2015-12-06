package nl.brusque.pinky;

import nl.brusque.pinky.promise.Promise;

public class Pinky {
    private final Promise _promise;

    public Pinky() {
        _promise = new Promise();
    }

    public IPromise getPromise() {
        return _promise;
    }

    public IPromise resolve(Object o) {
        return _promise.resolve(o);
    }

    public IPromise reject(Object o) {
        return _promise.reject(o.toString());
    }
}
