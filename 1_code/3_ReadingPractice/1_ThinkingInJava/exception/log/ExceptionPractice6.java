package exception.log;

import java.util.logging.*;
import java.io.*;

class LogAException extends Exception
{
    // ? how to understand the registration of the logger here?
    private static Logger logger = Logger.getLogger("LogAException");

    public LogAException()
    {
        StringWriter trace = new StringWriter();

        // ? what is doing here?
        printStackTrace(new PrintWriter(trace));

        logger.severe(trace.toString());
    }
}

class LogBException extends Exception
{
    private static Logger logger = Logger.getLogger("LogBException") ;

    public LogBException(String message)
    {
        StringWriter trace = new StringWriter();

        printStackTrace(new PrintWriter(trace));

        logger.severe(trace.toString() + "Message : " + message);
    }
}

public class ExceptionPractice6 
{
    public static void main(String[] args) 
    {
        try {
            throw new LogAException() ;
        } catch(LogAException e) {
            System.out.println("LogAException caught");
        }

        try {
            throw new LogBException("LogBException is thrown") ;
        } catch(LogBException e) {
            System.out.println("LogBException caught");
        }
    }
}

/**
 * result output:
 * 5月 09, 2025 10:35:25 下午 exception.log.LogAException <init>
 * 严重: exception.log.LogAException
 *         at exception.log.ExceptionPractice6.main(ExceptionPractice6.java:41)
 * 
 * LogAException caught
 * 5月 09, 2025 10:35:25 下午 exception.log.LogBException <init>
 * 严重: exception.log.LogBException
 *         at exception.log.ExceptionPractice6.main(ExceptionPractice6.java:47)
 * Message : LogBException is thrown
 * LogBException caught
 */