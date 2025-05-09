package exception.start;

class MyException extends Exception {
    public MyException() {}
    public MyException(String message)  { super(message); }
}

class ExceptionTrigger 
{
    public static void trigger() throws MyException 
    {
        throw new MyException("MyException triggered");
    }
}

public class ExceptionPractice4 
{
    public static void main(String[] args)
    {
        try {
            ExceptionTrigger.trigger() ;
        } catch (MyException e) {
            System.err.println("MyException: " + e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    } 
}

/**
 * result output:
 * MyException: MyException triggered
 * exception.MyException: MyException triggered
 *         at exception.ExceptionTrigger.trigger(ExceptionPractice4.java:12)
 *         at exception.ExceptionPractice4.main(ExceptionPractice4.java:21)
 */