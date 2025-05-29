package top.thesumst.tools;

import java.util.Scanner;

public class ScannerTools 
{
    static Scanner scanner = new Scanner(System.in);
    
    public static Scanner getScanner() 
    {
        return scanner;
    }

    public static void closeScanner() 
    {
        if (scanner != null) {
            scanner.close();
        }
    }
}
