package exception.log;

import java.util.logging.*;
import java.io.*;

class ExceptionLogger
{
    private Logger logger;
    
    public ExceptionLogger(String name)
    {
        logger = Logger.getLogger(name);
    }

    public void logSevere(Exception e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        logger.severe(sw.toString());
    }
}

public class ExceptionPractice7 
{
    public static void main(String[] args) 
    {
        ExceptionLogger exceptionLogger = new ExceptionLogger("Main");

        try {
            Integer[] list = {1, 2, 3, 4, 5} ;
            System.out.printf("the %dth element : %d\n", 5, list[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionLogger.logSevere(e);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}

/**
 * result output:
 * 5月 09, 2025 10:51:48 下午 exception.log.ExceptionLogger logSevere
 * 严重: java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
 *         at exception.log.ExceptionPractice7.main(ExceptionPractice7.java:31)
 */