package nl.brusque.pinky.promise;

public class PromiseStateHandler {
    private boolean _isResolved = false;
    private boolean _isPending  = true;
    private boolean _isRejected = false;
    private String _rejectionReason;
    private Object _resolvedWith;

    public boolean isPending() {
        return _isPending;
    }

    public boolean isResolved() {
        return _isResolved;
    }

    public boolean isRejected() {
        return _isRejected;
    }

    public void resolve(Object o) {
        _isResolved = true;

        _resolvedWith = o;
    }

    public void reject(String reason) {
        _isRejected = true;

        _rejectionReason = reason;
    }

    public Object getResolvedWith() {
        return _resolvedWith;
    }
}
