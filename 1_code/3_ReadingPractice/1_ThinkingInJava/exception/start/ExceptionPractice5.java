package exception.start;

class NotTimeException extends Exception 
{
    private int i ;

    public NotTimeException() {} 

    public NotTimeException(String message) 
    {
        super(message);
    }

    public NotTimeException(String message, int i) 
    {
        super(message);
        this.i = i;
    }

    @Override
    public String getMessage()
    {
        return String.format("it's still %d, %s", i, super.getMessage());
    }
}

class WaitingTime
{
    private int i ;

    public WaitingTime(int i)
    {
        this.i = i;
    }

    public void CheckTime() throws NotTimeException
    {
        /**
         * * Attention here:
         * * as here the throw exception operation is not  
         * * in try-catch block, and the custom Exception is not 
         * * extended from non-inspection exception,
         * * so it has to explicitly define what type of exceptions
         * * can be threw from this method
         * 
         * * or we can change the custom exception into the
         * * extension of RuntimeException, and then it is
         * * non-inspection exception. Then we can omitted 
         * * the exception explanation
         */
        if(i < 10)
            throw new NotTimeException("not time yet", i);
    }
}

public class ExceptionPractice5 
{
    public static void main(String[] args) 
    {
        int i = 0; 
        while(true)
        {
            try {
                WaitingTime waitingTime = new WaitingTime(i);
                waitingTime.CheckTime();

                System.out.printf("Now it's %d, and it's time to exit\n", i);
                break;
            } catch (NotTimeException e) {
                System.out.println("Caught NotTimeException :" + e.getMessage());
                i++;
            } catch (Exception e) {
                System.out.println("other exception");
                i++;
            }
        }
    }    
}

/**
 * result output:
 * Caught NotTimeException :it's still 0, not time yet                                                              
 * Caught NotTimeException :it's still 1, not time yet                                                              
 * Caught NotTimeException :it's still 2, not time yet
 * Caught NotTimeException :it's still 3, not time yet
 * Caught NotTimeException :it's still 4, not time yet
 * Caught NotTimeException :it's still 5, not time yet
 * Caught NotTimeException :it's still 6, not time yet
 * Caught NotTimeException :it's still 7, not time yet
 * Caught NotTimeException :it's still 8, not time yet
 * Caught NotTimeException :it's still 9, not time yet
 * Now it's 10, and it's time to exit
 */