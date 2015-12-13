package nl.brusque.pinky.helper;

public interface ISpy {
    int getCallCount();
    Object getCalledWith();
    Object call(Object o) throws Exception;
    ISpy returns(Object o);
    ISpy throwsError();
    long lastCall();
}