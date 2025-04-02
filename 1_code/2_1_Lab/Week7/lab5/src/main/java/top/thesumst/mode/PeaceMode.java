package top.thesumst.mode;

import top.thesumst.mode.component.Step;
import top.thesumst.type.ChessColor;
import java.awt.Point;

public class PeaceMode extends GameMode
{
    public PeaceMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        super(order, mode, size, name1, name2, color1, color2);


        int mid = size / 2 ;
        setChessColor(new Point(mid - 1, mid - 1), ChessColor.WHITE);
        setChessColor(new Point(mid - 1, mid), ChessColor.BLACK);
        setChessColor(new Point(mid, mid - 1), ChessColor.BLACK);
        setChessColor(new Point(mid, mid), ChessColor.WHITE);
    }

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param Point 下棋位置
     * @return boolean 是否成功
     */
    @Override
    public boolean receiveOperation(Point point) 
    {
        if(isOver)
        {
            return false;
        }
        return go(point);
    }

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param String 操作
     * 应该是pass或者quit，其中pass只有在ReversiMode中有用
     * @return boolean 是否成功
     */
    @Override
    public boolean receiveOperation(String operation) 
    {
        switch (operation) {
            case "quit":
                return true;
            case "pass":
                return false;
            default:
                return false;
        }
    }
    
    /**
     * * go方法，尝试落子
     * @param point 尝试落子位置
     * @return true 落子成功 false 落子失败
     */
    private boolean go(Point point) 
    {
        if(checkPoint(point))
        {
            ChessColor color = isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE ;
            addStep(new Step(point, color));
            setChessColor(point, color);
            isBlackTurn = !isBlackTurn ;
            isOver = checkGameOver() ;
            return true ;
        }
        return false ;
    }

    /**
     * * checkPoint方法，检查point位置是否可落子
     * @param point
     * @return true 可落子 false 不可落子
     */
    private boolean checkPoint(Point point)
    {
        if(getChessColor(point) == ChessColor.BLANK)
        {
            return true ;
        }
        return false ;
    }

    /**
     * * checkGameOver方法，检查游戏是否结束
     * @return true 游戏结束 false 游戏未结束
     */
    private boolean checkGameOver()
    {
        int blackChess = getChessNumber(ChessColor.BLACK) ;
        int whiteChess = getChessNumber(ChessColor.WHITE) ;
        if(blackChess + whiteChess == maxSize * maxSize) return true ;
        return false ;
    }
}