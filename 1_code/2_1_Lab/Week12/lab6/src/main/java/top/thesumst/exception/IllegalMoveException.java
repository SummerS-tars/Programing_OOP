package top.thesumst.exception;

public class IllegalMoveException extends Exception
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
