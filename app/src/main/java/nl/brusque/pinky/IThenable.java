package nl.brusque.pinky;

public interface IThenable {
    IPromise then(Object onFulfilled);
    IPromise then(Object onFulfilled, Object onRejected);
}
