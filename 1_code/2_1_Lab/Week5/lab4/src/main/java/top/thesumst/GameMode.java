package top.thesumst;

import java.awt.Point;

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

    private String getPlayerName(Player p)
    {
        return p.getName();
    }

    private ChessColor getPlayerChessColor(Player p)
    {
        return p.getColor();
    }

    /**
     * test output
     */
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
        PeaceMode peace = new PeaceMode(1, "peace", 8, "test1", "test2", ChessColor.BLACK, ChessColor.WHITE) ;
        System.out.printf("player1: name = %s, color = %c\nplayer2: name = %s, color = %c\n",
                        peace.getPlayerName(peace.player1), peace.getPlayerChessColor(peace.player1).getSymbol(),
                        peace.getPlayerName(peace.player2), peace.getPlayerChessColor(peace.player2).getSymbol());
        peace.printBoard();
        System.out.println(peace);

        PauseUtil.pause();

        for(int i = 0 ; i < 8 ; i++ ) 
        {
            for(int j = 0 ; j < 8 ; j++ )
            {
                PrintTools.clearConsole();
                peace.go(new Point(i, j));
                peace.printBoard();
                PauseUtil.pause();
            }
        }

        PrintTools.clearConsole();
        System.out.println("isOver = " + peace.isOver);
        PauseUtil.pause();
        peace.quit();
    }
}

class ReversiMode extends GameMode
{

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