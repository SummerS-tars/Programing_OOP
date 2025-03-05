package top.thesumst;

import java.awt.Point;

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
        // TODO: 增加彩色打印功能
        PrintTools printTools = new PrintTools();
        PrintTools.clearConsole();
        System.out.println("欢迎来到黑白棋游戏！");
        System.out.println("请输入任意按键以开始游戏");
        printTools.sc.nextLine();
    }

    /**
     * * game running main body
     */
    private static void runGame()
    {
        // TODO: 游戏运行主体编写
        ChessBoard chessBoardClass = new ChessBoard();
        chessBoardClass.initChessBoard();
        boolean keepGame = true;

        PrintTools printTools = new PrintTools();
        while (keepGame)
        {
            PrintTools.clearConsole();
            chessBoardClass.printChessBoard();
            Point attemptPoint = receiveGo();
            byte legalDirection = chessBoardClass.checkGo(attemptPoint);
            if (legalDirection == 0)
            {
                System.out.println("无效的下棋位置！请键入任何按键以重新选择");
                printTools.sc.nextLine();
                continue;
            }
            else
            {
                chessBoardClass.go(attemptPoint, legalDirection);
                chessBoardClass.updatePlayerChessNumber();
                if (!chessBoardClass.checkTurn()) keepGame = false;
            }
        }

        PrintTools.clearConsole();
        chessBoardClass.printChessBoard();
        System.out.println("游戏结束！");
        System.out.println("胜利者是：" + chessBoardClass.getWinner().getName() + chessBoardClass.getWinner().getColor().getSymbol());
    }

    /**
     * * receiveGo方法持续读入输入直到获取合法输入，并转换为符合chessBoard API的Point类实例
     * @return Point
     * 返回有效的Point格式下棋点
     */
    private static Point receiveGo()
    {
        // TODO: 计划接受乱序输入
        // TODO: 计划完善互动与提示信息



        Point attemptPoint = new Point();
        return attemptPoint;
    }


    /**
     * * checkReceive用于接收输入，确保输入处于合理范围，并根据一定逻辑返回操作
     * @return Point
     * 如果切换棋盘将
     */
    private static Point checkReceive()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.rememberCursor();

        while (true)
        {
            PrintTools.restoreCursor();
            PrintTools.clearConsoleAfterCursor();

            System.out.println("请输入你的下棋位置：");
            String input = printTools.sc.nextLine();
            if (input.length() != 2)
            {
                System.out.println("输入格式错误！请输入任何键以开始重新输入");
                printTools.sc.nextLine();
                continue;
            }
            char row = input.charAt(0);
            char col = input.charAt(1);
            if (row < '1' || row > '8' || col < 'A' || col > 'H')
            {
                System.out.println("输入格式错误！请输入任何键以开始重新输入");
                printTools.sc.nextLine();
                continue;
            }
            

            return new Point(row - 1, col -1) ;
        }
    }
}

class ReceiveTools
{
    private boolean changeFlag ;
    private int boardNum ;
    private Point goPoint ;

    ReceiveTools()
    {
        changeFlag = false ;
        boardNum = 0 ;
        goPoint = new Point() ;
    }

    public void setChangeFlag(boolean flag)
    {
        changeFlag = flag;
    }

    public boolean getChangeFlag()
    {
        return changeFlag ;
    } 
    
    public void setBoardNum(int num)
    {
        boardNum = num ;
    }

    public int getBoardNum()
    {
        return boardNum ;
    }

    public void setGoPoint(Point point)
    {
        goPoint.x = point.x ;
        goPoint.y = point.y ;
    }

    public Point getGoPoint()
    {
        return goPoint ;
    }

    public void receiveOperation()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.rememberCursor();

        while (true)
        {
            PrintTools.restoreCursor();
            PrintTools.clearConsoleAfterCursor();

            System.out.println("请输入你的下棋位置：");
            String input = printTools.sc.nextLine();
            int length = input.length();
            if (length > 2) // 输入太长，一定错误
            {
                System.out.println("输入格式错误！请输入任何键以开始重新输入");
                printTools.sc.nextLine();
                continue;
            }
            else if(length == 1)
            {
                if(!Character.isDigit(input.charAt(0))) // 非数字成立
                {
                    System.out.println("输入格式错误！请输入任何键以开始重新输入");
                    printTools.sc.nextLine();
                    continue;
                }
                else
                {
                    int num = Integer.parseInt(input) ;
                    if(num < 1 || num > 3) // 输入数字不在范围内
                    {
                        System.out.println("输入棋盘序号有误！请输入任何键以开始重新输入");
                        printTools.sc.nextLine();
                        continue;
                    }
                    else
                    {
                        setBoardNum(num) ;
                        setChangeFlag(true) ;
                        return ;
                    }
                }
            }
            else
            {
                char c1 = input.charAt(0) ;
                char c2 = input.charAt(1) ;

                if(c1 >= '1' && c1 <= '8' && (c2 >= 'A' && c2 <= 'H'|| c2 >= 'a' && c2 <= 'h')) // 输入合法
                {
                    setGoPoint(new Point(c1 - '1', (c2 - 'a' >= 0) ? c2 - 'a' : c2 - 'A')) ;
                    setChangeFlag(false) ;
                    return ;
                }
                else if(c2 >= '1' && c2 <= '8' && (c1 >= 'A' && c1 <= 'H'|| c1 >= 'a' && c1 <= 'h')) // 输入合法
                {
                    setGoPoint(new Point(c2 - '1', (c1 - 'a' >= 0) ? c1 - 'a' : c1 - 'A')) ;
                    setChangeFlag(false) ;
                    return ;
                }
                else
                {
                    System.out.println("输入格式错误！请输入任何键以开始重新输入");
                    printTools.sc.nextLine();
                    continue;
                }
            }
        }
    }
}

/**
 * test drive fot ReceiveTools
 */
class ReceiveToolsTestDrive
{
    public static void main(String[] args)
    {
        ReceiveTools receiveTools = new ReceiveTools();
        receiveTools.receiveOperation();
        if(receiveTools.getChangeFlag())
        {
            System.out.println("切换到棋盘" + receiveTools.getBoardNum());
        }
        else
        {
            System.out.println("下棋位置：" + receiveTools.getGoPoint().x + " " + receiveTools.getGoPoint().y);
        }
    }
}