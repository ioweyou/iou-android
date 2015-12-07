package nl.brusque.pinky;

public interface IThenable {
    IPromise then();
    IPromise then(Object onFulfilled);
    IPromise then(Object onFulfilled, Object onRejected);
}
