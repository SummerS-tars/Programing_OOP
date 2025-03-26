package top.thesumst;

class GameContainer 
{

}

class GameList
{
    private static 
    private List<GameMode> games = new ArrayList<GameMode>();

    GameList()
    {

        
    }    

    private void addGame(String gameMode)
    {
        switch (gameMode) {
            case "peace":
                games.add(new PeaceMode());
                break;
        
            default:
                break;
        }
    }
}

/**
 * * ReceiveTools类用于接收输入，根据输入内容返回操作 
 */
class ReceiveTools
{
    private boolean changeFlag ;    // 用于判断是否切换棋盘
    private int boardNum ;        // 用于存储切换的棋盘号   // ! ver2.0 fix try 1 : 注意此处棋盘号为从1开始，映射到数组下标时需要减1
    private Point goPoint ;       // 用于存储下棋位置
    private boolean endGameFlag ;       // 用于记录是否结束游戏

    ReceiveTools()
    {
        changeFlag = false ;
        boardNum = 0 ;
        goPoint = new Point() ;
        endGameFlag = false ;
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

    public void setEndGameFlag(boolean flag)
    {
        endGameFlag = flag ;
    }

    public boolean getEndGameFlag()
    {
        return endGameFlag;
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
            if (length > 2 || length == 0) // 输入太长，一定错误 // ! ver2.0 fix try 3 : 修正输入为空导致的崩溃
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
                    if(num < 1 || num > ReversiGame.boardSum) // 输入数字不在范围内
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

    /**
     * * receiveAllReviseOperation用于为keepRevise接受信息
     * * 输入exit退出游戏
     * * 输入有效棋盘数字则记录数字返回
     */
    public void receiveAllReviseOperation()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.rememberCursor();

        while( true )
        {
            PrintTools.restoreCursor();
            PrintTools.clearConsoleAfterCursor();

            System.out.println("请输入exit退出游戏或选择复盘其他棋盘：");
            String input = printTools.sc.nextLine();
            int length = input.length();
            if( input == "exit") // 输入exit退出游戏
            {
                endGameFlag = true ;
                return ;
            }
            else if( length == 1 && Character.isDigit(input.charAt(0)) ) // 输入数字进入棋盘序号有效性判断
            {
                int num = Integer.parseInt(input) ;
                if(num < 1 || num > ReversiGame.boardSum) // 输入数字不在范围内
                {
                    System.out.println("输入棋盘序号有误！请输入任何键以开始重新输入");
                    printTools.sc.nextLine();
                    continue;
                }
                else
                {
                    endGameFlag = false ;
                    boardNum = num ;
                    return ;
                }
            }
            else
            {
                System.out.println("输入无效！请输入任何键以重新输入");
                printTools.sc.nextLine();
                continue ;
            }
        }
    }

    /**
     * * receiveReviseOperation用于为reviseGame接受信息
     * * 输入有效棋盘数字记录数字返回
     */
    public void receiveReviseOperation()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.rememberCursor();

        while( true )
        {
            PrintTools.restoreCursor();
            PrintTools.clearConsoleAfterCursor();

            System.out.println("请选择棋盘：");
            String input = printTools.sc.nextLine();
            int length = input.length();
            if( length == 1 && Character.isDigit(input.charAt(0)) ) // 输入数字进入棋盘序号有效性判断
            {
                int num = Integer.parseInt(input) ;
                if(num < 1 || num > ReversiGame.boardSum) // 输入数字不在范围内
                {
                    System.out.println("输入棋盘序号无效！请输入任何键以开始重新输入");
                    printTools.sc.nextLine();
                    continue;
                }
                else
                {
                    boardNum = num ;
                    return ;
                }
            }
            else
            {
                System.out.println("输入有误！请输入任何键以重新输入");
                printTools.sc.nextLine();
                continue ;
            }
        }
    }
}