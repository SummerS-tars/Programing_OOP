package top.thesumst.exception;

public class IllegalCommandException extends RuntimeException
{
    public IllegalCommandException()
    {
        super("无效命令");
    }

    public IllegalCommandException(String message)
    {
        super(message);
    }
}
