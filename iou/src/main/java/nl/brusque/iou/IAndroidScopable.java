package nl.brusque.iou;

public interface IAndroidScopable  {
    AndroidPromise.ExecutionScope getExecutionScope();

    void setExecutionScope(AndroidPromise.ExecutionScope scope);
}
