package nl.brusque.androidiou;

import android.content.Context;

import nl.brusque.iou.AbstractIOU;

public class AndroidIOU extends AbstractIOU<AndroidPromise, AndroidFulfillable, AndroidRejectable> {
    private final AndroidPromise _promise;

    public AndroidIOU(Context context) {
        _promise = new AndroidPromise(context);
    }

    @Override
    public AndroidPromise getPromise() {
        return _promise;
    }
}
