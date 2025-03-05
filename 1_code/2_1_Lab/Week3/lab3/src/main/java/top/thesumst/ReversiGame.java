package top.thesumst;

import java.awt.Point;

public class ReversiGame
{
    int boardNum ; // 用于存储当前棋盘号
    int boardSum ; // 用于存储棋盘总数
    ChessBoard[] chessBoards ; // 用于存储多个棋盘
    boolean gameOver ; // 用于判断游戏是否结束

    // deprecated
    //// public static void main(String[] args)
    //// {
    ////     gameMotd();
    ////
    ////     ReversiGame reversiGame = new ReversiGame();
    ////     reversiGame.initGame();
    ////     reversiGame.runGame();
    //// }

    /**
     * * Welcome message of the game
     */
    public static void gameMotd()
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
     * * initGame方法用于初始化游戏
     */
    public void initGame()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.clearConsole(); 

        // 初始化：收集p1名字
        System.out.println("请输入1号玩家的名称：");
        String p1Name = printTools.sc.nextLine() ;
        
        // 初始化：收集p1棋子颜色
        ChessColor p1Color = ChessColor.BLACK ;
        boolean optionAvailable = false ;
        while(!optionAvailable)
        {
            PrintTools.clearConsole();
            System.out.println("请选择1号玩家的棋子颜色：(1.Black ○ | 2.White ● )");
            int op = printTools.sc.nextInt() ;
            printTools.sc.nextLine() ; // 读取缓冲区中多余的换行符
            switch (op)
            {
                case 1:
                    p1Color = ChessColor.BLACK ;
                    optionAvailable = true ;
                    break;
                case 2:
                    p1Color = ChessColor.WHITE ;
                    optionAvailable = true ;
                    break;
                default:
                    System.out.println("无效的选择！请键入任何按键以重新选择") ;
                    printTools.sc.nextLine() ;
                    break;
            }
        }

        // 初始化：收集p2名字
        System.out.println("请输入2号玩家的名称：");
        String p2Name = printTools.sc.nextLine() ;

        // 初始化：根据p1棋子颜色确定p2棋子颜色
        ChessColor p2Color = (p1Color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK ;

        // 实际初始化棋盘
        boardSum = 3 ;  // 定义棋盘总数，留一个接口，后面方便拓展
        chessBoards = new ChessBoard[boardSum] ;
        for(int i = 0; i < boardSum; i++)
        {
            chessBoards[i] = new ChessBoard(p1Name, p2Name, p1Color, p2Color) ;
        }

        // 初始化：收集初始棋盘号以及游戏状态
        boardNum = 0 ;
        gameOver = false ;
    }

    /**
     * * game running main body
     */
    public void runGame()
    {
        ReceiveTools receiveTools = new ReceiveTools();

        // ! 游戏主体
        boolean keepGame = true;
        while (keepGame)
        {
            // TODO: 添加切换棋盘接收部份 over
            // TODO: 棋盘完赛可以预览，但是之后只能换棋盘，不能再下棋
            // TODO: 添加结束复盘功能
            
            PrintTools.clearConsole();
            chessBoards[boardNum].printChessBoard();

            // 接受操作输入（包括下棋位置和切换棋盘）
            receiveTools.receiveOperation();

            // 执行操作（切换棋盘或传入位置）
            Point attemptPoint = new Point();
            if(receiveTools.getChangeFlag())
            {
                boardNum = receiveTools.getBoardNum() ;
                continue ;
            }
            else attemptPoint = receiveTools.getGoPoint();
            
            // 位置传入棋盘，判断后进行相关操作
            inputPoint(attemptPoint);
        }

        PrintTools.clearConsole();
        chessBoards[boardNum].printChessBoard();
        System.out.println("游戏结束！");
        System.out.println("胜利者是：" + chessBoards[boardNum].getWinner().getName() + chessBoards[boardNum].getWinner().getColor().getSymbol());
    }

    private void inputPoint(Point attemptPoint)
    {
        PrintTools printTools = new PrintTools();
        byte legalDirection = chessBoards[boardNum].checkGo(attemptPoint);
        if (legalDirection == 0)
        {
            System.out.println("无效的下棋位置！请键入任何按键以重新选择");
            printTools.sc.nextLine();
        }
        else
        {
            chessBoards[boardNum].go(attemptPoint, legalDirection);
            chessBoards[boardNum].updatePlayerChessNumber();

            // ? 原用于判断是否继续游戏，现因增加多棋盘机制，需要更改游戏结束逻辑
            // if (!chessBoards[boardNum].checkTurn()) keepGame = false;
        }
    }

    /**
     * * checkGameOver方法用于检查游戏是否结束
     * @return boolean
     * 所有棋盘都完成游戏则返回true，否则返回false
     */
    private boolean checkGameOver()
    {
        for(int i = 0; i < boardSum; i++)
        {
            if(!chessBoards[i].getGameEndFlag()) return false ;
        }
        return true ;
    }
}

/**
 * * ReceiveTools类用于接收输入，根据输入内容返回操作 
 */
class ReceiveTools
{
    private boolean changeFlag ;    // 用于判断是否切换棋盘
    private int boardNum ;        // 用于存储切换的棋盘号
    private Point goPoint ;       // 用于存储下棋位置

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

    /**
     * * receiveOperation方法用于接收输入，根据输入内容返回操作
     * * 如果输入为1-3，则返回切换棋盘操作
     * * 如果输入为A1-H8，则返回下棋位置（接受大小写和乱序输入）
     * * 如果输入不符合规范，则重新输入
     */
    public void receiveOperation()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.rememberCursor();

        // * 循环输入直至输入合法
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
            else    // 判断为试图输入位置，判断是否有效位置
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
                    System.out.println("输入位置无效！请输入任何键以开始重新输入");
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