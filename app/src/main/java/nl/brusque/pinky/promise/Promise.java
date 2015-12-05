package nl.brusque.pinky.promise;

import nl.brusque.pinky.IPromise;
import nl.brusque.pinky.IRunnable;

public abstract class Promise implements IPromise, IRunnable {
    private PromiseStateHandler _promiseState = new PromiseStateHandler();
    private IPromise _then;

    public void resolve(final Object o) {
        new Thread() {
            @Override
            public void run() {
                _promiseState.resolve(o);

                if(_then!=null) {
                    _then.run(o);
                }
            }
        }.run();
    }

    public IPromise then(IPromise r) {
        _then = r;

        if (_promiseState.isResolved()) {
            _then.resolve(_then.run(_promiseState.getResolvedWith()));
        }

        return _then;
    }
}
