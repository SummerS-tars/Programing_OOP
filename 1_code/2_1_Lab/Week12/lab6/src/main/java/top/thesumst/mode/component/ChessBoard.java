package top.thesumst.mode.component;

import top.thesumst.type.ChessStatement;
import java.awt.Point;

public class ChessBoard
{
    private ChessStatement[][] chessBoard ;
    final int size ;

    public ChessBoard(int size)
    {
        this.size = size ;
        chessBoard = new ChessStatement[size][size] ;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                chessBoard[i][j] = ChessStatement.BLANK ;
            }
        }
    }

    /**
     * * setChessColor方法，设置point位置棋盘棋子
     * @param point
     * @param color
     */
    public void setChessStatement(Point point , ChessStatement color)
    {
        chessBoard[point.x][point.y] = color ;
    }

    /**
     * * getChessColor方法，获取point位置棋盘棋子颜色
     * @param point
     * @return ChessColor
     */
    public ChessStatement getChessStatement(Point point)
    {
        return chessBoard[point.x][point.y] ;
    }

    /**
     * * getChessNumber方法，获取棋盘上特定颜色的棋子数量
     * @param ChessStatement 查询的颜色
     * @return int 棋子数量
     */
    public int getChessNumber(ChessStatement color)
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
