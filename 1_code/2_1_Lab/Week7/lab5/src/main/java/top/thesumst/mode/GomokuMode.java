package top.thesumst.mode;

import top.thesumst.mode.component.*;
import top.thesumst.type.*;
import java.awt.Point;
import java.util.EnumSet;

public class GomokuMode extends GameMode
{
    private Player winner ;

    public GomokuMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        super(order, mode, size, name1, name2, color1, color2);

        winner = null ;
    }

    @Override
    public boolean receiveOperation(Point point) 
    {

    }

    @Override
    public boolean receiveOperation(String operation)
    {
        switch(operation) {
            case "quit":
                return true;
            default:
                return false;
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

    private boolean go(Point point)
    {
        if(checkPoint(point))
        {
            ChessColor color = isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE ;
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
        return false;
    }

    private boolean checkPoint(Point point)
    {
        if(getChessColor(point) == ChessColor.BLANK)
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
        // TODO: 检测当前落子是否满足胜利条件，已完成，待测试
        EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
        int[] linkedNumber = {1, 1, 1, 1};
        for(Direction direction : directions)
        {
            Point focus = new Point(point) ;
            while(moveFocus(focus, direction))
            {
                if(getChessColor(focus) == (isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE))
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
                if(getChessColor(new Point(i, j)) == ChessColor.BLANK)  // * 有空位，说明未满
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
        winner = (player1.getColor() == (isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE) )
                ? player1 : player2;
    }

    public static void main(String[] args) 
    {
        Direction[] directions = Direction.values();
        for(Direction direction : directions)
        {
            System.out.println(direction.ordinal());
        }
    }
}
