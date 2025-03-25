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
        return gameOrder + " " + gameMode;
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