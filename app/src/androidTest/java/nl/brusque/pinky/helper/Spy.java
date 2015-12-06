package nl.brusque.pinky.helper;

public class Spy implements ISpy {
    private Object _result;
    private boolean _throwsError;
    private final int[] _callCount = {0};
    private final Object[] _calledWith = {null};

    public void increaseCallCount() {
        _callCount[0]++;
    }

    public void updateCalledWith(Object o) {
        _calledWith[0] = o;
    }

    public int getCallCount() {
        return _callCount[0];
    }

    @Override
    public Object getCalledWith() {
        return _calledWith[0];
    }

    public Object call(Object o) throws Exception {
        increaseCallCount();
        updateCalledWith(o);

        if (_throwsError) {
            throw new Exception();
        }

        return _result;
    }

    public ISpy returns(Object o) {
        _result = o;

        _throwsError = false;

        return this;
    }

    public ISpy throwsError() {
        _throwsError = true;

        return this;
    }
}