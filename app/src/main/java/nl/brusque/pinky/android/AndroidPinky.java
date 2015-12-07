package nl.brusque.pinky.android;

import android.content.Context;

import nl.brusque.pinky.AbstractPromise;

public class AndroidPinky {
    private final AndroidPromise _promise;

    public AndroidPinky(Context context) {
        _promise = new AndroidPromise(context);
    }

    public AndroidPromise getPromise() {
        return _promise;
    }

    public AbstractPromise<AndroidPromise> resolve(Object o) {
        return _promise.resolve(o);
    }

    public AbstractPromise<AndroidPromise> reject(Object o) {
        return _promise.reject(o.toString());
    }
}
