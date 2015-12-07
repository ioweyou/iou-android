package nl.brusque.pinky.android;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import nl.brusque.pinky.AbstractPromise;
import nl.brusque.pinky.IFulfillable;

public class AndroidPromise extends AbstractPromise<AndroidPromise> {

    public enum AndroidExecutionScope {
        BACKGROUND,
        UI
    }

    private final Context _context;

    public AndroidPromise(Context context) {
        _context = context;
    }

    @Override
    public boolean isFulfillable(Object onFulfilled) {
        return onFulfilled != null && onFulfilled instanceof AndroidFulfillable;
    }

    @Override
    public boolean isRejectable(Object onRejected) {
        return onRejected != null && onRejected instanceof AndroidRejectable;
    }

    @Override
    public Object runFulfill(final IFulfillable fulfillable, final Object o) throws Exception {
        if (!isFulfillable(fulfillable)) {
            return null;
        }

        final AndroidFulfillable androidFulfillable = (AndroidFulfillable)fulfillable;
        if (androidFulfillable.getExecutionScope().equals(AndroidExecutionScope.UI)) {
            final List<Exception> exceptions = new ArrayList<>();
            final List<Object> results = new ArrayList<>();
            new AsyncTask<Object, Void, Object>() {

                @Override
                protected Object doInBackground(Object... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Object result) {
                    try {
                        results.add(fulfillable.fulfill(o));
                    } catch (Exception e) {
                        exceptions.add(e);
                    }

                    results.add(result);
                }
            }.get();

            if (exceptions.size() > 0) {
                throw exceptions.get(0);
            }

            return results.get(0);
        }

        return fulfillable.fulfill(o);
    }

    @Override
    public Object runReject(IRejectable rejectable, Object o) throws Exception {
        if (!isRejectable(rejectable)) {
            return null;
        }

        return rejectable.reject(o);
    }

    @Override
    public AndroidPromise create() {
        return new AndroidPromise(_context);
    }
}
