import java.util.Scanner;

public class PrimeNumbers {
    /**
     * *test Interface
     * @param args
     */
    public static void main( String[] args )
    {
        java.util.Scanner sc = new java.util.Scanner(System.in) ;

        System.out.println("请选择测试的函数:");
        System.out.println("1. 列出1至100间的所有素数\n2. 格式化输出1至指定数字间的所有素数");
        
        byte i = sc.nextByte() ;
        switch( i )
        {
            case 1 : findPrimeNumbers1() ; break ;
            case 2 : findPrimeNumbers2() ; break ;
            default : break ;
        }
    }
    
    /**
     * *Find all prime numbers between 1 and 100
     */
    static void findPrimeNumbers1() 
    {
        System.out.println("Prime numbers list between 1 and 100 :");
        for( int i = 2 ; i <= 100 ; i ++ )
        {
            boolean isPrime = true ;
            for( int j = 2 ; j * j <= i ;  j ++ )
                if( i % j == 0 )
                {
                    isPrime = false ;
                    break ;
                }
            
            if( isPrime ) System.out.println(i) ;
        }
    }

    static void findPrimeNumbers2()
    {
        java.util.Scanner sc = new java.util.Scanner(System.in) ;
        System.out.println("请输入一个数:");
        int max = sc.nextInt() ;
        

        System.out.println("Prime numbers list between 1 and " + max) ;
        int count = 0 ;
        for( int i = 2 ; i <= max ; i ++ )
        {
            boolean isPrime = true ;
            for( int j = 2 ; j * j <= i ; j ++ )
                if( i % j == 0 )
                {
                    isPrime = false ;
                    break ;
                }
            
            if( isPrime )
            {
                count ++ ;
                System.out.print(i + " ") ;
                if( count % 8 == 0 && count / 8 != 0)
                    System.out.println("") ;
            }
        }
    }
}
