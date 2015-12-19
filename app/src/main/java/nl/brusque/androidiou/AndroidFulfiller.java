package nl.brusque.androidiou;

import android.app.Activity;
import android.content.Context;

import nl.brusque.iou.DefaultFulfiller;

public class AndroidFulfiller extends DefaultFulfiller<AndroidFulfillable> {
    private final Context _context;

    public AndroidFulfiller(Context context) {
        _context = context;
    }

    @Override
    public Object fulfill(final AndroidFulfillable fulfillable, final Object o) throws Exception {
        if (fulfillable.getExecutionScope().equals(AndroidPromise.ExecutionScope.BACKGROUND)) {
            return super.fulfill(fulfillable, o);
        }

        final Object[] result = new Object[1];
        final Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    result[0] = AndroidFulfiller.super.fulfill(fulfillable, o);

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
