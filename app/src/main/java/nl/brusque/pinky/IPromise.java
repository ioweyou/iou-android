package nl.brusque.pinky;

public interface IPromise extends IThenable, IRunnable {

    void resolve(Object run);
}
