package nl.brusque.iou;

import android.app.Activity;
import android.content.Context;

public class AndroidThenCaller extends DefaultThenCallable<AndroidThenCallable> {
    private final Context _context;

    public AndroidThenCaller(Context context) {
        _context = context;
    }

    @Override
    public Object call(final AndroidThenCallable fulfillable, final Object o) throws Exception {
        if (fulfillable.getExecutionScope().equals(AndroidPromise.ExecutionScope.BACKGROUND)) {
            return super.call(fulfillable, o);
        }

        final Object[] result = new Object[1];
        final Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    result[0] = AndroidThenCaller.super.call(fulfillable, o);

                    synchronized (this) {
                        this.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ((Activity)_context).runOnUiThread(uiRunnable);

        uiRunnable.wait();

        return result[0];
    }
}
