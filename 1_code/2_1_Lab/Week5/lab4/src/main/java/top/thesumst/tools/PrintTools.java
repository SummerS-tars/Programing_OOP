package top.thesumst.tools;

import java.util.Scanner;

public class PrintTools 
{
    public Scanner sc = new Scanner(System.in) ;

    /**
     * * clearConsole方法用于清空控制台
     */
    public static void clearConsole()
    {
        /**
         * * 转义字符说明：
         * * \033 ESC (ASCII 27)
         * * [H 光标位置命令，将光标移回屏幕左上角（行1，列1）
         * * [2J 清屏
         * 
         * * System.out.flush() 用于确保输出生效
         */
        System.out.print("\033[H\033[2J") ;
        System.out.flush();
    }

    /**
     * * rememberCursor方法用于记录当前光标位置
     */
    public static void rememberCursor()
    {
        System.out.print("\033[s") ;
        System.out.flush();
    }

    /**
     * * restoreCursor方法用于恢复光标位置
     */
    public static void restoreCursor()
    {
        System.out.print("\033[u") ;
        System.out.flush();
    }

    /**
     * * clearConsoleAfterCursor方法用于清除光标位置之后的内容
     */
    public static void clearConsoleAfterCursor()
    {
        System.out.print("\033[J") ;
        System.out.flush();
    }
}

class PrintToolsTestDrive
{
    public static void main(String[] args)
    {
        PrintTools.clearConsole();
        System.out.print("Hello World!");
        PrintTools.rememberCursor();
        System.out.print("Hello World!");
        PrintTools.restoreCursor();
        System.out.print("Hello World!");
        PrintTools.clearConsoleAfterCursor();
    }
}