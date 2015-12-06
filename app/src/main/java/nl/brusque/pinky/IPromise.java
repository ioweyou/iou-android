package nl.brusque.pinky;

public interface IPromise extends IThenable {
    IPromise resolve(Object run);
    IPromise reject(Object o);
}
