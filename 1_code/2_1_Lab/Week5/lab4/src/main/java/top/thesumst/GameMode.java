package top.thesumst;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;

abstract class GameMode 
{
    final int gameOrder ;
    final String gameMode ;
    final int maxSize ;
    protected ChessBoard board;
    protected Player player1, player2;
    protected boolean isBlackTurn, isOver;

    GameMode(int order, String mode,int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        gameOrder = order;
        gameMode = mode;
        maxSize = size;
        board = new ChessBoard(maxSize);
        player1 = new Player(name1, color1);
        player2 = new Player(name2, color2);
        isBlackTurn = true;
        isOver = false;
    }

    /**
     * * quit方法，退出游戏
     */
    void quit()
    {
        System.exit(0);
    }

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param Point 下棋位置
     */
    abstract void receiveOperation(Point point) ;

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param String 操作
     * 应该是pass或者quit，其中pass只有在ReversiMode中有用
     */
    abstract void receiveOperation(String operation) ;

    /**
     * * toString方法，返回游戏模式信息
     * @return String 游戏模式信息  
     */
    @Override
    public String toString() 
    {
        return gameOrder + ". " + gameMode;
    }
}

class PeaceMode extends GameMode
{
    PeaceMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        super(order, mode, size, name1, name2, color1, color2);
    }

    @Override
    void receiveOperation(Point point) 
    {
        // TODO：完善游戏结束判断及信息显示
        if(isOver)
        {
            System.out.println("游戏已结束");
            return ;
        }
        go(point);
    }

    // TODO: 完善receiveOperation方法
    @Override
    void receiveOperation(String operation) 
    {
        switch (operation) {
            case "quit":
                quit();
                break;
            case "pass":
                System.out.println("peace模式不支持pass");
                break;
            default:
                System.out.println("无效操作");
                break;
        }
    }
    
    void go(Point point) 
    {
        if(checkPoint(point))
        {
            ChessColor putColor = isBlackTurn ? player1.getColor() : player2.getColor() ;
            board.setChessColor(point, putColor);
            isBlackTurn = !isBlackTurn ;
            isOver = checkGameOver() ;
        }
    }

    /**
     * * checkPoint方法，检查point位置是否可落子
     * @param point
     * @return true 可落子 false 不可落子
     */
    private boolean checkPoint(Point point)
    {
        if(board.getChessColor(point) == ChessColor.BLANK)
        {
            return true ;
        }
        System.out.println("输入无效，当前位置已有棋子");
        PauseUtil.pause();
        return false ;
    }

    /**
     * * checkGameOver方法，检查游戏是否结束
     * @return true 游戏结束 false 游戏未结束
     */
    private boolean checkGameOver()
    {
        int blackChess = board.getChessNumber(ChessColor.BLACK) ;
        int whiteChess = board.getChessNumber(ChessColor.WHITE) ;
        if(blackChess + whiteChess == maxSize * maxSize) return true ;
        return false ;
    }

    /**
     * test output
     */
    private String getPlayerName(Player p)
    {
        return p.getName();
    }
    private ChessColor getPlayerChessColor(Player p)
    {
        return p.getColor();
    }
    private void printBoard()
    {
        for(int i = 0; i < maxSize; i++)
        {
            for(int j = 0; j < maxSize; j++)
                System.out.print(board.getChessColor(new Point(i, j)).getSymbol()+" ") ;
            System.out.println();
        }     
    }

    /**
     * * main方法用于测试
     * @param args
     */
    public static void main(String[] args) 
    {
        PeaceMode peace = new PeaceMode(1, "peace", 8, 
                    "test1", "test2", ChessColor.BLACK, ChessColor.WHITE) ;

        System.out.printf("player1: name = %s, color = %c\nplayer2: name = %s, color = %c\n",
                        peace.getPlayerName(peace.player1), peace.getPlayerChessColor(peace.player1).getSymbol(),
                        peace.getPlayerName(peace.player2), peace.getPlayerChessColor(peace.player2).getSymbol());
        peace.printBoard();
        System.out.println(peace);
        PauseUtil.pause();

        peace.receiveOperation("pass");
        peace.receiveOperation("test");
        PauseUtil.pause();

        for(int i = 0 ; i < 8 ; i++ ) 
        {
            for(int j = 0 ; j < 8 ; j++ )
            {
                PrintTools.clearConsole();
                peace.receiveOperation(new Point(i, j));
                peace.printBoard();
                PauseUtil.pause();
            }
        }

        PrintTools.clearConsole();
        System.out.println("isOver = " + peace.isOver);
        PauseUtil.pause("游戏结束，按回车键退出");
        peace.receiveOperation("quit");
    }
}

class ReversiMode extends GameMode
{
    private Map<Point, Byte> validPointsCache ;
    private boolean shouldPass ;

    ReversiMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        super(order, mode, size, name1, name2, color1, color2);
        validPointsCache = new HashMap<>();
        shouldPass = false ;
        refreshValidPoints();
    }

    @Override
    void receiveOperation(Point point) 
    {
        // TODO：完善游戏结束判断及信息显示
        if(isOver)
        {
            PauseUtil.pause("游戏已结束，请按回车键后输入其他有效操作");
            return ;
        }

        if(shouldPass)
        {
            PauseUtil.pause("当前玩家无可下棋位置，请按回车键后输入其他操作");
            return ;
        }

        go(point);
    }

    @Override
    void receiveOperation(String operation)
    {
        // TODO: 测试pass操作
        switch (operation) {
            case "pass":
                if(shouldPass)
                {
                    shouldPass = false ;
                    if(isOver)
                    {
                        PauseUtil.pause("游戏结束，请按回车键后输入其他操作");
                    }
                    else
                    {
                        isBlackTurn = !isBlackTurn ;
                        checkGameOver() ;
                    }
                }
                else PauseUtil.pause("当前玩家有合法位置，请按回车键后输入其他操作");
                break;
            case "quit":
                quit();
                break;
            default:
                PauseUtil.pause("无效操作，请按回车键后重新输入");
                break;
        }
    }

    private void go(Point point)
    {
        if(checkGo(point))
        {
            reverse(point, validPointsCache.get(point));
            updatePlayerChessNumber();
            isBlackTurn = !isBlackTurn ;
            refreshValidPoints();
            shouldPass = validPointsCache.isEmpty() ;
        }
        else
        {
            // 只有在非有效位置时才提示错误
            PauseUtil.pause("非有效位置，请按回车键后重新输入");
        }
    }

    /**
     * * checkGo方法检查输入point是否存在于validPointsCache中
     * @param point 尝试下棋位置
     * @return true 存在 false 不存在
     */
    private boolean checkGo(Point point)
    {
        return validPointsCache.containsKey(point) ;
    }

    /**
     * * refreshValidPoints方法，刷新当前玩家可下棋位置
     * * 同时刷新棋盘上的提示信息
     */
    private void refreshValidPoints()
    {
        clearValidPointsHint();
        validPointsCache.clear();
        
        for(int i = 0; i < maxSize; i++)
        {
            for(int j = 0; j < maxSize; j++)
            {
                Point point = new Point(i, j) ;
                byte legalDirection = checkPoint(point) ;
                if(legalDirection != 0)
                {
                    validPointsCache.put(point, legalDirection) ;
                }
            }
        }
        refreshValidPointsHint();
    }

    /**
     * * checkPoint方法实现对玩家输入的棋步合法性检验
     * @param point 要检查的位置
     * @return byte 合法方向信息串
     */
    public byte checkPoint(Point point)
    {
        byte legalDirection = 0 ;
        /**
         * 检测条件构思：
         * 0. 不超出棋盘（或许在输入转换方法中实现）
         * 1. 周围八格棋子检验 & 记录有异色棋子方向
         *      next direction 无相邻异色棋子 + 记录0不变
         *      next direction 有相邻异色棋子 + 改记录1
         *      out 无相邻异色棋子
         *      go on 有相邻异色棋子
         * 2. 异色棋子方向检验 & 记录包夹方向
         *      next direction 无同色棋子夹住 + 改记录0
         *      next direction 有同色棋子夹住 + 记录1不变
         *      out 无包夹成功方向
         *      go on 有包夹成功方向
         */

        // * 当前位置已经有棋子，非法步骤
        if(getChessColor(point) != ChessColor.BLANK) return legalDirection ;

        // * 遍历八个方向寻找是否有合法方向
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            boolean legalFlag = false ;
            Point focus = new Point(point) ;
            while(legalFlag = moveFocus(focus, direction))
            {
                // * 判断是否异色
                if(getChessColor(focus) == (isBlackTurn ? ChessColor.WHITE : ChessColor.BLACK))
                {
                    // * 是异色棋子，维持该方向位数字为1
                    legalDirection |= direction.getValue() ;
                }
                else
                {
                    if(getChessColor(focus) == ChessColor.BLANK) legalFlag = false ; // * 该方向为空，将legalFlag置为false再退出
                    break ;
                    /**
                     * 注意：此处包含多种情况
                     * 1. 该方向相邻异色棋子且有同色棋子包夹，保持 legalFlag:true 方向位:1 进入下一个方向
                     * 2. 该方向直接为空或者相邻异色棋子之后为空，设置 legalFlag:false 经过方向位重置后 进入下一个方向
                     * 3. 该方向相邻同色棋子， 保持 legalFlag:true 方向位:0 进入下一个方向
                     */
                }
            }
            if(!legalFlag) legalDirection &= ~(direction.getValue()) ; // * 处理走到底或者出界的情况，确保该方向位为0
        }
        return legalDirection ;
    }

    /**
     * * clearValidPoints方法，清除地图中的提示信息
     */
    private void clearValidPointsHint()
    {
        for(Point point : validPointsCache.keySet())
            if(board.getChessColor(point) == ChessColor.VALID)
                board.setChessColor(point, ChessColor.BLANK);
    }

    /**
     * * refreshValidPointsHint方法，刷新地图中中的提示信息
     */
    private void refreshValidPointsHint()
    {
        for(Point point : validPointsCache.keySet())
            board.setChessColor(point, ChessColor.VALID);
    }

    /**
     * * reverse方法，实施棋子翻转
     * @param point
     * @param legalDirection
     */
    private void reverse(Point point, byte legalDirection)
    {
        // * 本次运行要放置的棋子颜色设置
        ChessColor color = isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE ;

        // * 棋子翻转
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            // * 判断是否为有效方向
            if((direction.getValue() & legalDirection) == 0 ) continue ;
            else
            {
                Point focus = new Point(point) ;
                moveFocus(focus, direction) ;

                // * 该方向为有效方向，开始翻转棋子
                while(getChessColor(focus) != color)
                {
                    setChessColor(focus, color);
                    moveFocus(focus, direction);    // * 此处抛弃了返回值，因为有效方向一定会有同色棋子包夹
                }
            }
        }

        // * 原位置补上棋子
        setChessColor(point, color) ;
    }

    /**
     * * moveFocus移动检查焦点
     * @param focus
     * @param direction
     * @return boolean
     * true : successfully move
     * false : unable to move
     */
    private boolean moveFocus(Point focus , Direction direction)
    {
        int dx = Direction.getDirectionDelta(direction).x ;
        int dy = Direction.getDirectionDelta(direction).y ;


        if( focus.x + dx >= 0 && focus.x + dx < maxSize &&
            focus.y + dy >= 0 && focus.y + dy < maxSize)
        {
            focus.x += dx ;
            focus.y += dy ;
            return true ;
        }
        else return false ;
    }

    /**
     * * updatePlayerChessNumber方法，更新玩家棋子数量
     */
    private void updatePlayerChessNumber()
    {
        player1.setChessNumber(board.getChessNumber(player1.getColor()));
        player2.setChessNumber(board.getChessNumber(player2.getColor()));
    }

    /**
     * * checkGameOver方法，检查游戏是否结束
     * * 在每次pass后触发，如果未找到有效位置，说明双方均无法下棋，游戏结束
     */
    private void checkGameOver()
    {
        refreshValidPoints();
        if(validPointsCache.isEmpty())
            isOver = true ;
    }

    /**
     * * getChessColor方法，获取point位置棋盘棋子颜色
     * @param point
     * @return ChessColor
     */
    public ChessColor getChessColor(Point point)
    {
        return board.chessBoard[point.x][point.y] ;
    }

    /**
     * * setChessColor方法，设置point位置棋盘棋子
     * @param point
     * @param color
     */
    public void setChessColor(Point point , ChessColor color)
    {
        board.chessBoard[point.x][point.y] = color ;
    }
    

    /**
     * test output
     */
    private String getPlayerName(Player p)
    {
        return p.getName();
    }
    private ChessColor getPlayerChessColor(Player p)
    {
        return p.getColor();
    }
    private void printBoard()
    {
        for(int i = 0; i < maxSize; i++)
        {
            for(int j = 0; j < maxSize; j++)
                System.out.print(board.getChessColor(new Point(i, j)).getSymbol()+" ") ;
            System.out.println();
        }     
    }

    public static void main(String[] args) 
    {
        ReversiMode reversi = new ReversiMode(1, "reversi", 8, 
                            "test1", "test2", ChessColor.BLACK, ChessColor.WHITE) ;    

        System.out.println("test part 1");
        System.out.printf("player1: name = %s, color = %c\nplayer2: name = %s, color = %c\n",
                        reversi.getPlayerName(reversi.player1), reversi.getPlayerChessColor(reversi.player1).getSymbol(),
                        reversi.getPlayerName(reversi.player2), reversi.getPlayerChessColor(reversi.player2).getSymbol());
        reversi.printBoard();
        System.out.println(reversi);
        PauseUtil.pause();

        reversi.receiveOperation("pass");
        reversi.receiveOperation("test");
        PauseUtil.pause();

        while(!reversi.isOver)
        {
            PrintTools.clearConsole();
            System.out.println("test part 2");
            
            // 无论是否要跳过，都显示当前棋盘和玩家信息
            reversi.printBoard();
            System.out.printf("[Player1]%s %c : %d\n[Player2]%s %c : %d\n",
                            reversi.getPlayerName(reversi.player1), reversi.getPlayerChessColor(reversi.player1).getSymbol(),reversi.player1.getChessNumber(),
                            reversi.getPlayerName(reversi.player2), reversi.getPlayerChessColor(reversi.player2).getSymbol(),reversi.player2.getChessNumber());
            System.out.println("当前玩家：" + (reversi.isBlackTurn ? reversi.player1.getName() : reversi.player2.getName())
                                + " " + (reversi.isBlackTurn ? reversi.player1.getColor().getSymbol() : reversi.player2.getColor().getSymbol()));
            
            if(!reversi.shouldPass)
            {
                // 随机选择一个有效位置
                Point randomPoint = null;
                if (!reversi.validPointsCache.isEmpty()) {
                    // 使用更简洁的方法随机选择一个点
                    java.util.List<Point> points = new java.util.ArrayList<>(reversi.validPointsCache.keySet());
                    randomPoint = points.get((int)(Math.random() * points.size()));
                }
                
                System.out.println("randomPoint = " + randomPoint);
                if (randomPoint != null) {
                    reversi.receiveOperation(randomPoint);
                    // 已经在go方法中有处理，这里不需要再次暂停
                }
            }
            else 
            {
                System.out.println("当前玩家无法下棋，执行pass操作");
                reversi.receiveOperation("pass");
            }
            
            PauseUtil.pause("按回车继续...");
        }

        PrintTools.clearConsole();
        System.out.println("isOver = " + reversi.isOver);
        PauseUtil.pause("游戏结束，按回车键退出");
        reversi.receiveOperation("quit");
    }
}

class Player
{
    private String name ;
    private ChessColor color ;
    private int chessNumber ;

    Player(String name, ChessColor color)
    {
        this.name = name ;
        this.color = color ;
        chessNumber = 2 ;   // * 注意一开始每个玩家都有两个棋子
    }

    public String getName()
    {
        return name ;
    }
        
    public ChessColor getColor() 
    {
        return color;
    }

    public void setChessNumber(int num)
    {
        chessNumber = num ;
    }

    public int getChessNumber()
    {
        return chessNumber ;
    }
    
    // TODO: 或许可以加上 toString() 方法
}

class ChessBoard
{
    ChessColor[][] chessBoard ;
    int size ;

    ChessBoard(int size)
    {
        this.size = size ;
        chessBoard = new ChessColor[size][size] ;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                chessBoard[i][j] = ChessColor.BLANK ;
            }

            int mid = size / 2 ;
            chessBoard[mid - 1][mid - 1] = ChessColor.WHITE ;
            chessBoard[mid - 1][mid] = ChessColor.BLACK ;
            chessBoard[mid][mid - 1] = ChessColor.BLACK ;
            chessBoard[mid][mid] = ChessColor.WHITE ;
        }
    }

    /**
     * * setChessColor方法，设置point位置棋盘棋子
     * @param point
     * @param color
     */
    void setChessColor(Point point , ChessColor color)
    {
        chessBoard[point.x][point.y] = color ;
    }

    /**
     * * getChessColor方法，获取point位置棋盘棋子颜色
     * @param point
     * @return ChessColor
     */
    ChessColor getChessColor(Point point)
    {
        return chessBoard[point.x][point.y] ;
    }

    /**
     * * getChessNumber方法，获取棋盘上特定颜色的棋子数量
     * @param ChessColor 查询的颜色
     * @return int 棋子数量
     */
    int getChessNumber(ChessColor color)
    {
        int count = 0 ;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(chessBoard[i][j] == color)
                {
                    count++ ;
                }
            }
        }
        return count ;
    }
}