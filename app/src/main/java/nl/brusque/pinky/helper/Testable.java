package nl.brusque.pinky.helper;

import nl.brusque.pinky.IPromise;

public abstract class Testable implements Runnable {
    private IPromise _p;

    protected void setPromise(IPromise p) {
        _p = p;
    }

    protected IPromise getPromise() {
        return _p;
    }
}