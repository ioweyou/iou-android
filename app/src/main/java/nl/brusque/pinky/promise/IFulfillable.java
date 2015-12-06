package nl.brusque.pinky.promise;

public interface IFulfillable {
    Object fulfill(final Object o) throws Exception;
}
