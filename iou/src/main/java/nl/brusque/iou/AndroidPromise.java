package nl.brusque.iou;

import android.content.Context;

public class AndroidPromise extends AbstractPromise<AndroidPromise, AndroidThenCallable, AndroidThenCallable> {
    private final Context _context;

    public enum ExecutionScope {
        UI,
        BACKGROUND
    }

    public AndroidPromise(Context context) {
        super(AndroidThenCallable.class, AndroidThenCallable.class, null, null, new AndroidThenCaller(context) , new AndroidThenCaller(context));

        _context = context;
    }

    @Override
    protected AndroidPromise create() {
        return new AndroidPromise(_context);
    }

    @Override
    public AndroidPromise then() {
        return then(null, null);
    }

    @Override
    public AndroidPromise then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    @Override
    public AndroidPromise then(Object onFulfilled, Object onRejected) {
        return (AndroidPromise)super.then(onFulfilled, onRejected);
    }

    public AndroidPromise fail(Object onRejected) {
        return then(null, onRejected);
    }
}
