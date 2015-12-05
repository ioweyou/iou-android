package nl.brusque.iou;

import android.app.Activity;

public class IOUAndroid<TFulfill> extends AbstractIOU<TFulfill> {
    private final AndroidPromise<TFulfill> _promise;

    public IOUAndroid(Activity activity) {
        _promise = new AndroidPromise<>(activity);
    }


    @Override
    public AndroidPromise<TFulfill> getPromise() {
        return _promise;
    }

    public AndroidPromise<TFulfill> when(TFulfill o) {
        resolve(o);

        return _promise;
    }
}
