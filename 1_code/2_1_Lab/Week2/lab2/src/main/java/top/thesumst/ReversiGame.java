package top.thesumst;

import java.util.*;
import java.awt.Point ;

public class ReversiGame
{
    /**
     * ! 总程序入口
     * @param args
     */
    public static void main(String[] args)
    {

        gameMotd();
        runGame();
    }

    /**
     * * Welcome message of the game
     */
    private static void gameMotd()
    {
        // TODO: 完善MOTD



        PrintTools printTools = new PrintTools() ;
        PrintTools.clearConsole();
        System.out.println("欢迎来到黑白棋游戏！") ;
        System.out.println("请输入任意按键以开始游戏") ;
        printTools.sc.nextLine() ;
    }

    /**
     * * game running main body
     */
    private static void runGame()
    {
        // TODO: 游戏运行主体编写
        ChessBoard chessBoardClass = new ChessBoard() ;
        chessBoardClass.initChessBoard();
        boolean keepGame = true;

        PrintTools printTools = new PrintTools() ;
        while(keepGame)
        {
            PrintTools.clearConsole();
            chessBoardClass.printChessBoard();
            Point attemptPoint = receiveGo() ;
            byte legalDirection = chessBoardClass.checkGo(attemptPoint) ;
            if(legalDirection == 0)
            {
                System.out.println("无效的下棋位置！请键入任何按键以重新选择") ;
                printTools.sc.nextLine() ;
                continue ;
            }
            else
            {
                chessBoardClass.go(attemptPoint, legalDirection) ;
                chessBoardClass.updatePlayerChessNumber();
                if(!chessBoardClass.checkTurn()) keepGame = false ;
            }
        }

        PrintTools.clearConsole();
        chessBoardClass.printChessBoard();
        System.out.println("游戏结束！") ;
        System.out.println("胜利者是：" + chessBoardClass.getWinner().getName() + chessBoardClass.getWinner().getColor().getSymbol()) ;
    }

    /**
     * * receiveGo方法持续读入输入直到获取合法输入，并转换为符合chessBoard API的Point类实例
     * @return Point
     * 返回有效的Point格式下棋点
     */
    private static Point receiveGo()
    {
        // TODO: 接受当局玩家输入（并转换为api入口）
        // TODO: 计划接受乱序输入
        // TODO: 计划完善互动与提示信息
        
        
        PrintTools printTools = new PrintTools() ;
        PrintTools.rememberCursor();

        Point attemptPoint = new Point() ;
        boolean validInput = false ;
        while(!validInput)
        {
            PrintTools.restoreCursor();
            PrintTools.clearConsoleAfterCursor();
            System.out.println("请输入你的下棋位置：");
            String input = printTools.sc.nextLine() ;
            if(input.length() != 2)
            {
                System.out.println("输入格式错误！请输入任何键以开始重新输入") ;
                printTools.sc.nextLine() ;
                continue ;
            }
            char row = input.charAt(0) ;
            char col = input.charAt(1) ;
            if(row < 'A' || row > 'H' || col < '1' || col > '8')
            {
                System.out.println("输入格式错误！请输入任何键以开始重新输入") ;
                printTools.sc.nextLine() ;
                continue ;
            }
            attemptPoint.x = row - 'A' ;
            attemptPoint.y = col - '1' ;
            validInput = true ;
        }
        return attemptPoint ;
    }
}
