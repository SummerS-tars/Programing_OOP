package top.thesumst.type;

/**
 * Operation, a class used to wrap the operation
 * which will be sent to GameMode by the Command classes
 * 
 * @param <T> the type of data associated with the operation
 */
public class Operation<T> 
{
    OperationType type;
    T data;

    public Operation(OperationType type, T data)
    {
        this.type = type;
        this.data = data;
    }

    public OperationType getType()
    {
        return type;
    }

    public T getData()
    {
        return data;
    }
}
