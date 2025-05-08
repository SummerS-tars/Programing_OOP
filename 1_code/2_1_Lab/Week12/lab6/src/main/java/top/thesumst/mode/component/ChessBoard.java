package top.thesumst.mode.component;

import top.thesumst.type.ChessColor;
import java.awt.Point;

public class ChessBoard
{
    private ChessColor[][] chessBoard ;
    final int size ;

    public ChessBoard(int size)
    {
        this.size = size ;
        chessBoard = new ChessColor[size][size] ;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                chessBoard[i][j] = ChessColor.BLANK ;
            }
        }
    }

    /**
     * * setChessColor方法，设置point位置棋盘棋子
     * @param point
     * @param color
     */
    public void setChessColor(Point point , ChessColor color)
    {
        chessBoard[point.x][point.y] = color ;
    }

    /**
     * * getChessColor方法，获取point位置棋盘棋子颜色
     * @param point
     * @return ChessColor
     */
    public ChessColor getChessColor(Point point)
    {
        return chessBoard[point.x][point.y] ;
    }

    /**
     * * getChessNumber方法，获取棋盘上特定颜色的棋子数量
     * @param ChessColor 查询的颜色
     * @return int 棋子数量
     */
    public int getChessNumber(ChessColor color)
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
