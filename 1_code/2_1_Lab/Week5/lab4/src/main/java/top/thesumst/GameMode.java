package top.thesumst;

import java.awt.Point;



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