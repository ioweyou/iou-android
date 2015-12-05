package nl.brusque.pinky.promise;

import nl.brusque.pinky.IPromise;
import nl.brusque.pinky.IRunnable;

public abstract class Promise implements IPromise, IRunnable {
    private final boolean _runInBackground;
    private PromiseStateHandler _promiseState = new PromiseStateHandler();
    private IPromise _then;

    public Promise() {
        this(true);
    }

    public Promise(boolean runInBackground) {
        _runInBackground = runInBackground;
    }

    public void resolve(Object o) {
        _promiseState.resolve(o);

        if (_then!=null) {
            _then.run(o);
        }
    }

    public IPromise then(IPromise r) {
        _then = r;

        if (_promiseState.isResolved()) {
            _then.resolve(_then.run(_promiseState.getResolvedWith()));
        }

        return _then;
    }
}
