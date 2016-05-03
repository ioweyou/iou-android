package nl.brusque.iou;

public abstract class AndroidThenCallable<T, R> implements IThenCallable<T, R>, IAndroidScopable {
    private AndroidPromise.ExecutionScope _scope;

    public AndroidThenCallable() {
        this(AndroidPromise.ExecutionScope.BACKGROUND);
    }

    public AndroidThenCallable(AndroidPromise.ExecutionScope scope) {
        _scope = scope;
    }

    @Override
    public AndroidPromise.ExecutionScope getExecutionScope() {
        return _scope;
    }

    @Override
    public void setExecutionScope(AndroidPromise.ExecutionScope scope) {
        _scope = scope;
    }
}
