package nl.brusque.iou;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

class AndroidThenCallableStrategy<T, R> extends AbstractThenCallableStrategy<T, R> {
    private final Activity _activity;

    AndroidThenCallableStrategy(Activity context) {
        _activity = context;
    }

    @Override
    <TFullfill, TResult> IThenCallable<TFullfill, TResult> convert(final IThenCallable<TFullfill, TResult> thenCallable) throws Exception {
        if (thenCallable instanceof AndroidThenCallable) {
            return thenCallable;
        }

        return new AndroidThenCallable<TFullfill, TResult>() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public TResult apply(TFullfill o) throws Exception {
                return thenCallable.apply(o);
            }
        };
    }

    private <TAnything> R call(final IThenCallable<TAnything, R> thenCallable, final TAnything o) throws Exception {
        if (!(thenCallable instanceof AndroidThenCallable)) {
            throw new IllegalArgumentException("callable is not of type AndroidThenCallable");
        }

        AndroidThenCallable<T, R> androidThenCallable = (AndroidThenCallable<T, R>)thenCallable;

        if (androidThenCallable.getExecutionScope().equals(AndroidPromise.ExecutionScope.BACKGROUND)) {
            return thenCallable.apply(o);
        }

        final List<R> result = new ArrayList<>(1);
        final Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        result.add(0, thenCallable.apply(o));

                        this.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            _activity.runOnUiThread(uiRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        uiRunnable.wait();

        return result.get(0);
    }

    @Override
    R reject(final IThenCallable<Object, R> thenCallable, final Object o) throws Exception {
        return call(convert(thenCallable), o);
    }

    @Override
    R resolve(final IThenCallable<T, R> thenCallable, final T o) throws Exception {
        return call(convert(thenCallable), o);
    }

}
