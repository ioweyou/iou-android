package nl.brusque.iou;

public abstract class AndroidScopedThenCallable extends AndroidThenCallable {
    private final AndroidPromise.ExecutionScope _scope;

    public AndroidScopedThenCallable(AndroidPromise.ExecutionScope scope) {
        _scope = scope;
    }

    @Override
    public AndroidPromise.ExecutionScope getExecutionScope() {
        return _scope;
    }
}
