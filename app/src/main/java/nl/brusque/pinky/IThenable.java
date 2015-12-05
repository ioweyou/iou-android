package nl.brusque.pinky;

public interface IThenable {
    IPromise then(IPromise r);
}
