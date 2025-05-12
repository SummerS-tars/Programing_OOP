package top.thesumst.mode;

import top.thesumst.mode.component.*;
import top.thesumst.type.*;
import top.thesumst.exception.*;
import java.awt.Point;
import java.util.*;

public class GomokuMode extends GameMode
{
    private Player winner ;

    public GomokuMode(int order, String mode, int size, String name1, String name2, ChessStatement color1, ChessStatement color2)
    {
        super(order, mode, size, name1, name2, color1, color2);
        setBarrier();
        winner = null ;
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
                throw new IllegalCommandException("gomoku模式不支持pass操作") ;
            case BOMB:
                Point bombPoint = (Point) operation.getData();
                return useBomb(bombPoint);
            default:
                throw new IllegalCommandException("不支持的操作类型");
        }
    }

    /**
     * * getWinner方法，获取赢家
     * @return Player
     * 返回赢家(如果存在)，如果平局，返回null
     */
    public Player getWinner()
    {
        return winner;
    }

    private boolean go(Point point) throws IllegalMoveException
    {
        if(checkPoint(point))
        {
            ChessStatement color = isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE ;
            addStep(new Step(point, color));
            setChessColor(point, color);

            // * 检察游戏状态
            if(checkLink(point))
            {
                setWinner();
                isOver = true;
                return true;
            }
            if(checkFull())
            {
                isOver = true;
                return true;
            }

            isBlackTurn = !isBlackTurn;
            return true;
        }
        throw new IllegalMoveException("落子位置不合法");
    }

    private boolean checkPoint(Point point)
    {
        if(getChessStatement(point) == ChessStatement.BLANK)
        {
            return true ;
        }
        return false ;
    }

    /**
     * * checkLine方法，检查当前落子是否满足胜利条件
     * @param point
     * @return boolean
     * true表示满足胜利条件
     * false表示不满足胜利条件
     */
    private boolean checkLink(Point point)
    {
        EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
        int[] linkedNumber = {1, 1, 1, 1};
        for(Direction direction : directions)
        {
            Point focus = new Point(point) ;
            while(moveFocus(focus, direction))
            {
                if(getChessStatement(focus) == (isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE))
                {
                    linkedNumber[direction.ordinal() % 4]++ ;
                }
                else
                {
                    break;
                }
            }

            if(linkedNumber[direction.ordinal() % 4] >= 5)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * * checkFull方法，检查棋盘是否已满
     * @return boolean
     * true表示棋盘已满
     * false表示棋盘未满
     */
    private boolean checkFull()
    {
        for(int i = 0 ; i < maxSize ; i ++)
            for(int j = 0 ; j < maxSize ; j ++)
                if(getChessStatement(new Point(i, j)) == ChessStatement.BLANK)  // * 有空位，说明未满
                    return false;
        return true;
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
     * * setWinner方法，在监测到胜利条件时，将当前回合玩家设置为赢家
     */
    private void setWinner()
    {
        winner = (player1.getColor() == (isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE) )
                ? player1 : player2;
    }

    private void setBarrier()
    {
        Set<Point> barrierSet = new HashSet<Point>() ;
        Random random = new Random() ;
        int barrierNumber = 4 ;

        while(barrierSet.size() < barrierNumber)
        {
            int x = random.nextInt(maxSize) ;
            int y = random.nextInt(maxSize) ;
            Point point = new Point(x, y) ;
            barrierSet.add(point) ;
        }

        for(Point p : barrierSet)
            setChessColor(p, ChessStatement.BARRIER);
    }
}
