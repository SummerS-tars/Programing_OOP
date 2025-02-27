package top.thesumst;

import java.util.Scanner;

public class Practice2 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in) ;
        byte t = sc.nextByte() ;
        System.out.println("The Byte Is : " + t ) ;
        sc.close();
    }
}

/**
 * 报错示例：   
 * 输入：128
 * 报错：
 * 128
 * Exception in thread "main" java.util.InputMismatchException: Value out of range. Value:"128" Radix:10
 *      at java.base/java.util.Scanner.nextByte(Scanner.java:2034)
 *      at java.base/java.util.Scanner.nextByte(Scanner.java:1982)
 *      at top.thesumst.Practice2.main(Practice2.java:10)
 */
