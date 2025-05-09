package exception.start;

class ExceptionPractice1 
{
    public static void main(String[] args)
    {
        try
        {
            throw new Exception("This is an exception");
        } catch(Exception e)
        {
            /**
             * * getMessage() used to get the message of the exception
             * * witch is defined in Throwable class as detailedMessage
             */
            System.err.println("Caught an exception: " + e.getMessage());
        }
    }
}