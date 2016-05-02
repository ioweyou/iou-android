package nl.brusque.iou;

import android.app.Activity;

public class AndroidPromise<TFulfill> extends AbstractPromise<TFulfill> {
    private final Activity _activity;

    public enum ExecutionScope {
        UI,
        BACKGROUND
    }

    public AndroidPromise(Activity activity) {
        super(new AndroidThenCallableStrategy(activity));

        _activity = activity;
    }

    @Override
    protected <TAnythingFulfill> AndroidPromise<TAnythingFulfill> create() {
        return new AndroidPromise<>(_activity);
    }

    public <TAnythingOutput> IThenable<TAnythingOutput> fail(IThenCallable<Object, TAnythingOutput> failureCallable) {
        return super.then(null, failureCallable);
    }

    @Override
    public <TAnythingOutput> AndroidPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
        return (AndroidPromise<TAnythingOutput>)super.then(onFulfilled);
    }

    @Override
    public <TAnythingOutput> AndroidPromise<TAnythingOutput> then(
            IThenCallable<TFulfill, TAnythingOutput> onFulfilled,
            IThenCallable<Object, TAnythingOutput> onRejected) {

        return (AndroidPromise<TAnythingOutput>)super.then(onFulfilled, onRejected);
    }
}
