package top.thesumst.exception;

public class IllegalMoveException extends RuntimeException
{
    public IllegalMoveException()
    {
        super("无效的移动");
    }

    public IllegalMoveException(String message)
    {
        super(message);
    }
}
