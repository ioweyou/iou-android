package nl.brusque.androidiou;

import android.content.Context;

import nl.brusque.iou.AbstractPromise;

public class AndroidPromise extends AbstractPromise<AndroidPromise, AndroidFulfillable, AndroidRejectable> {
    private final Context _context;

    public enum ExecutionScope {
        UI,
        BACKGROUND
    }

    public AndroidPromise(Context context) {
        super(AndroidFulfillable.class, AndroidRejectable.class, null, null, new AndroidFulfiller(context) , new AndroidRejector(context));

        _context = context;
    }

    @Override
    protected AndroidPromise create() {
        return new AndroidPromise(_context);
    }

    @Override
    public AbstractPromise<AndroidPromise, AndroidFulfillable, AndroidRejectable> resolve(final Object o) {
        //get

        return super.resolve(o);
    }
}
