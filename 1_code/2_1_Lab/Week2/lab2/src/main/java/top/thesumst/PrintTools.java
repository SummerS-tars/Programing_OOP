package top.thesumst;

public class PrintTools
{
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
}

