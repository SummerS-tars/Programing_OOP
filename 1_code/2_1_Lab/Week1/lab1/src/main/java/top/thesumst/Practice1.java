package top.thesumst;

public class Practice1 
{
    public static void main(String[] args)
    {
        double miles = 100 ;

        final double KILOMETERS_PER_MILE = 1.609 ;

        double kilometers ;
        kilometers = miles * KILOMETERS_PER_MILE ;

        System.out.println("The kilometers are : " + kilometers);
    }
}