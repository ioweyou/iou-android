package nl.brusque.pinky.helper;

import android.util.Log;

public class Spy implements ISpy {
    private Object _result;
    private boolean _throwsError;
    private final int[] _callCount = {0};
    private final Object[] _calledWith = {null};
    private long _lastCall = 0;

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

    public long lastCall() {
        return _lastCall;
    }

    public Object call(Object o) throws Exception {
        Thread.sleep(1);
        Log.w("CALL", o!=null ? String.valueOf(o) : "NULL");
        increaseCallCount();
        updateCalledWith(o);
        _lastCall = System.currentTimeMillis();

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