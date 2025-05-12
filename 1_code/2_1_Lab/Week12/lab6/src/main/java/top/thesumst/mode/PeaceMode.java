package top.thesumst.mode;

import top.thesumst.mode.component.Step;
import top.thesumst.type.ChessStatement;
import top.thesumst.type.*;
import top.thesumst.exception.*;
import java.awt.Point;

public class PeaceMode extends GameMode
{
    public PeaceMode(int order, String mode, int size, String name1, String name2, ChessStatement color1, ChessStatement color2)
    {
        super(order, mode, size, name1, name2, color1, color2);


        int mid = size / 2 ;
        setChessColor(new Point(mid - 1, mid - 1), ChessStatement.WHITE);
        setChessColor(new Point(mid - 1, mid), ChessStatement.BLACK);
        setChessColor(new Point(mid, mid - 1), ChessStatement.BLACK);
        setChessColor(new Point(mid, mid), ChessStatement.WHITE);
    }

    /**
     * * receiveOperation方法，接收操作
     * @param operation
     * @return boolean
     * @throws IllegalMoveException
     * @throws IllegalCommandException
     */
    @Override
    public boolean receiveOperation(Operation<?> operation)
    throws IllegalMoveException, IllegalCommandException
    {
        OperationType type = operation.getType();
        switch (type) {
            case MOVE:
                if(isOver) throw new IllegalMoveException("游戏已经结束，无法下棋") ;
                Point point = (Point) operation.getData();
                return go(point);
            case PASS:
                throw new IllegalCommandException("peace模式不支持pass操作") ;
            case BOMB:
                throw new IllegalCommandException("peace模式不支持炸弹操作") ;
            default:
                throw new IllegalCommandException("不支持的操作类型");
        }
    }
    
    /**
     * * go方法，尝试落子
     * @param point 尝试落子位置
     * @return true 落子成功 false 落子失败
     */
    private boolean go(Point point) throws IllegalMoveException
    {
        if(checkPoint(point))
        {
            ChessStatement color = isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE ;
            addStep(new Step(point, color));
            setChessColor(point, color);
            isBlackTurn = !isBlackTurn ;
            isOver = checkGameOver() ;
            return true ;
        }
        throw new IllegalMoveException("落子位置不合法") ;
    }

    /**
     * * checkPoint方法，检查point位置是否可落子
     * @param point
     * @return true 可落子 false 不可落子
     */
    private boolean checkPoint(Point point)
    {
        if(getChessStatement(point) == ChessStatement.BLANK)
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
        int blackChess = getChessNumber(ChessStatement.BLACK) ;
        int whiteChess = getChessNumber(ChessStatement.WHITE) ;
        if(blackChess + whiteChess == maxSize * maxSize) return true ;
        return false ;
    }
}