package nl.brusque.pinky.android;

import android.app.Activity;
import android.content.Context;

import nl.brusque.pinky.IPromise;
import nl.brusque.pinky.promise.Promise;

public class AndroidPromise implements IPromise {
    private final AndroidExecutionScope _scope;
    private final Context _context;
    private final IPromise _promise;
    private AndroidPromise _nextPromise;

    enum AndroidExecutionScope {
        UI,
        BACKGROUND
    }

    public AndroidPromise(Context context, AndroidExecutionScope scope) {
        _scope   = scope;

        _context = context;

        _promise = new Promise();
    }

    @Override
    public AndroidPromise resolve(final Object run) {
        if (_scope == AndroidExecutionScope.UI) {
            ((Activity)_context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _promise.resolve(run);
                }
            });
        }

        return this;
    }

    @Override
    public AndroidPromise reject(final Object o) {
        if (_scope == AndroidExecutionScope.UI) {
            ((Activity)_context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _promise.reject(o);
                }
            });
        }

        return this;
    }

    @Override
    public AndroidPromise then() {
        _promise.then();

        //_nextPromise = new AndroidPromise();

        //return new AndroidPromise();
        return null;
    }

    @Override
    public AndroidPromise then(Object onFulfilled)
    {
        return null;
        //return _promise.then(onFulfilled);
    }

    @Override
    public IPromise then(Object onFulfilled, Object onRejected) {
        return _promise.then(onFulfilled, onRejected);
    }


}
