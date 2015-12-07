package nl.brusque.pinky.android;

import android.content.Context;

public class AndroidPinky {
    private final AndroidPromise _promise;

    public AndroidPinky(Context context) {
        _promise = new AndroidPromise(context, AndroidPromise.AndroidExecutionScope.BACKGROUND);
    }

    public AndroidPromise getPromise() {
        return _promise;
    }

    public AndroidPromise resolve(Object o) {
        return _promise.resolve(o);
    }

    public AndroidPromise reject(Object o) {
        return _promise.reject(o.toString());
    }
}
