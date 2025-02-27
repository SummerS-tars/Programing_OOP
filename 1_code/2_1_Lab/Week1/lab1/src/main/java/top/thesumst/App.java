package top.thesumst;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        App app = new App() ;
        long time = app.getSysTime() ;
        app.printTime(time) ;
    }

    private long getSysTime()
    {
        return System.currentTimeMillis() ;
    }

    private void printTime( long time ) 
    {
        time /= 1000 ;
        int hours , minutes , seconds ;
        hours = minutes = seconds = 0 ;
        seconds =(int) (time % 60) ;
        time /= 60 ;
        minutes =(int)(time % 60 ) ;
        time /= 60 ;
        hours = (int)( time % 24 ) ;
        System.out.printf("the time now is %d : %d : %d\n" , hours , minutes , seconds ) ;
    }
}
