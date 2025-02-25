package top.thesumst;
import java.util.Scanner;

public class SimpleCalculator {
    public static void main( String args[] ) {
        Scanner sc = new Scanner(System.in) ;

        try {
            System.out.println("Welcome to use the SimpleCalculator!");
            System.out.println("Please enter the first number : ") ;
            double num1 = sc.nextDouble() ;

            System.out.println("Please enter the second number : ") ;
            double num2 = sc.nextDouble() ;

            System.out.println("Please choose an operator : (+,-,*,/)") ;
            char op = sc.next().charAt(0) ;

            double result = 0 ;
            switch ( op ) {
                case '+' :
                    result = num1 + num2 ;
                    break ;
                case '-' :
                    result = num1 - num2 ;
                    break ;
                case '*' :
                    result = num1 * num2 ;
                    break ;
                case '/' :
                    if( num2 != 0 ) {
                        result = num1 / num2 ;
                        break ;
                    } else {
                        System.out.println("Exit with error : the divisor is 0") ;
                        break ;
                    }
                default :
                    System.out.println("Exit with error : unavailable operation is chosen !" ) ;
                    return ;
            }

            System.out.println("the result is :" + result ) ;
            return ;
        } finally {
            sc.close() ;
        }
    }
}
