package top.thesumst;

public class Practice3 
{
    public static void main( String[] args ) 
    {
        // * test convert from float/double to integer
        double d1 , d2 ;
        d1 = 1.245 ;
        d2 = -23.56 ;
        int i1 , i2 ;
        i1 = (int) d1 ;
        i2 = (int) d2 ;
        System.out.printf("d1 = %f | d2 = %f\n" , d1 , d2 ) ;
        System.out.printf("i1 = %d | i2 = %d\n" , i1 , i2 ) ;
    }
}

/**
 * output : i1 = 1 | i2 = -23
 * 说明：浮点数向整数转换，直接去掉小数部分，不论正负
 */