package nl.brusque.androidiou;

import android.app.Activity;
import android.content.Context;

import nl.brusque.iou.DefaultRejector;

public class AndroidRejector extends DefaultRejector<AndroidRejectable> {
    private final Context _context;

    public AndroidRejector(Context context) {
        _context = context;
    }

    @Override
    public Object reject(final AndroidRejectable rejectable, final Object o) throws Exception {
        if (rejectable.getExecutionScope().equals(AndroidPromise.ExecutionScope.BACKGROUND)) {
            return super.reject(rejectable, o);
        }

        final Object[] result = new Object[1];
        final Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    result[0] = AndroidRejector.super.reject(rejectable, o);

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
