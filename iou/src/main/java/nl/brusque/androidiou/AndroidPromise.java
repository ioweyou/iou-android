package nl.brusque.androidiou;

import android.content.Context;

import nl.brusque.iou.AbstractPromise;

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
}
