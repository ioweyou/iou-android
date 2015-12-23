package nl.brusque.iou;

import android.content.Context;

public class AndroidIOU extends AbstractIOU<AndroidPromise, AndroidThenCallable, AndroidThenCallable> {
    private final AndroidPromise _promise;

    public AndroidIOU(Context context) {
        _promise = new AndroidPromise(context);
    }

    @Override
    public AndroidPromise getPromise() {
        return _promise;
    }

    public AndroidPromise resolve(Object o) {
        return (AndroidPromise)super.resolve(o);
    }

    public AndroidPromise reject(Object o) {
        return (AndroidPromise)super.reject(o);
    }

    public AndroidPromise when(Object o) {
        return resolve(o);
    }

}
