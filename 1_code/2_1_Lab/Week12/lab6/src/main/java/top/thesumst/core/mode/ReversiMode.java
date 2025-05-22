package top.thesumst.core.mode;

import top.thesumst.type.*;
import top.thesumst.type.component.Player;
import top.thesumst.type.component.Step;
import top.thesumst.type.exception.*;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;

public class ReversiMode extends GameMode
{
    private Map<Point, Byte> validPointsCache ;
    private boolean shouldPass ;
    private Player winner ;

    public ReversiMode(int order, String mode, int size, String name1, String name2, ChessStatement color1, ChessStatement color2)
    {
        super(order, mode, size, name1, name2, color1, color2);

        int mid = size / 2 ;
        setChessColor(new Point(mid - 1, mid - 1), ChessStatement.WHITE);
        setChessColor(new Point(mid - 1, mid), ChessStatement.BLACK);
        setChessColor(new Point(mid, mid - 1), ChessStatement.BLACK);
        setChessColor(new Point(mid, mid), ChessStatement.WHITE);

        validPointsCache = new HashMap<>();
        shouldPass = false ;
        refreshValidPoints();
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
                if(shouldPass) throw new IllegalMoveException("当前玩家无有效下棋位置") ;
                Point point = (Point) operation.getData();
                return go(point);
            case PASS:
                throw new IllegalCommandException("gomoku模式不支持pass操作") ;
            case BOMB:
                throw new IllegalCommandException("reversi模式不支持炸弹操作") ;
            default:
                throw new IllegalCommandException("不支持的操作类型");
        }
    }

    // ! 以下public方法必须将GameMode向下转型为ReversiMode才能调用
    /**
     * shouldPass方法，返回当前玩家是否需要pass
     * @return boolean
     */
    public boolean shouldPass()
    {
        return shouldPass ;
    }
    /**
     * getWinner方法，获取胜者
     * @return Player 胜者，若平局则返回null
     */
    public Player getWinner()
    {
        return winner ;
    }

    /**
     * * go方法，尝试落子
     * @param point 尝试落子位置
     */
    private boolean go(Point point) throws IllegalMoveException
    {
        if(checkGo(point))
        {
            ChessStatement color = isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE ;
            addStep(new Step(point, color));
            reverse(point, validPointsCache.get(point));
            updatePlayerChessNumber();

            // TODO: update game state: should pass or end game
            updateGameState() ;

            return true ;
        }
        throw new IllegalMoveException("不是合法下棋位置") ;
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
     * * checkPoint方法实现对玩家输入的棋步合法性检验
     * @param point 要检查的位置
     * @return byte 合法方向信息串
     */
    private byte checkPoint(Point point)
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
        if(getChessStatement(point) != ChessStatement.BLANK) return legalDirection ;

        // * 遍历八个方向寻找是否有合法方向
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            boolean legalFlag = false ;
            Point focus = new Point(point) ;
            while(legalFlag = moveFocus(focus, direction))
            {
                // * 判断是否异色
                if(getChessStatement(focus) == (isBlackTurn ? ChessStatement.WHITE : ChessStatement.BLACK))
                {
                    // * 是异色棋子，维持该方向位数字为1
                    legalDirection |= direction.getValue() ;
                }
                else
                {
                    if(getChessStatement(focus) == ChessStatement.BLANK) legalFlag = false ; // * 该方向为空，将legalFlag置为false再退出
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
     * * refreshValidPoints方法，刷新当前玩家可下棋位置
     * * 同时刷新棋盘上的提示信息
     */
    private void refreshValidPoints()
    {
        clearValidPointsHint();
        validPointsCache.clear();
        
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
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
     * * clearValidPoints方法，清除地图中的提示信息
     */
    private void clearValidPointsHint()
    {
        for(Point point : validPointsCache.keySet())
            if(getChessStatement(point) == ChessStatement.VALID)
                setChessColor(point, ChessStatement.BLANK);
    }

    /**
     * * refreshValidPointsHint方法，刷新地图中中的提示信息
     */
    private void refreshValidPointsHint()
    {
        for(Point point : validPointsCache.keySet())
            setChessColor(point, ChessStatement.VALID);
    }

    /**
     * * reverse方法，实施棋子翻转
     * @param point
     * @param legalDirection
     */
    private void reverse(Point point, byte legalDirection)
    {
        // * 本次运行要放置的棋子颜色设置
        ChessStatement color = isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE ;

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
                while(getChessStatement(focus) != color)
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


        if( focus.x + dx >= 0 && focus.x + dx < size &&
            focus.y + dy >= 0 && focus.y + dy < size)
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
        player1.setChessNumber(getChessNumber(player1.getColor()));
        player2.setChessNumber(getChessNumber(player2.getColor()));
    }

    /**
     * * checkGameOver方法，检查游戏是否结束
     * * 在每次pass后触发，如果未找到有效位置，说明双方均无法下棋，游戏结束
     * @return true 游戏结束 false 游戏未结束
     */
    // private boolean checkGameOver()
    // {
    //     refreshValidPoints();
    //     if(validPointsCache.isEmpty())
    //         return true;
    //     return false;
    // }

    /**
     * * setWinner方法，在监测到胜利条件时，设置棋子更多的一方为赢家
     * * 如果棋子数量相同，则设置为null
     */
    private void setWinner()
    {
        winner = player1.getChessNumber() > player2.getChessNumber() ? player1 :
        player1.getChessNumber() < player2.getChessNumber() ? player2 : null ;
    }

    /**
     * * updateGameState方法，更新游戏状态
     */
    private void updateGameState()
    {
        // 换手
        isBlackTurn = !isBlackTurn ;
        refreshValidPoints() ;

        // * 检查游戏是否结束
        if(validPointsCache.isEmpty())
        {
            /**
             * * 逻辑解释：
             * * 若已经有一方pass过，且此时还是没有有效位置
             * * 那么游戏结束
             * * 准备设置赢家以表示游戏结束
             */
            if(shouldPass) // * 当前玩家pass后另一方没有合法位置
            {
                isOver = true ;
                setWinner() ;
                return ;
            }
            else // * 正常下棋后另一方没有合法位置
            {
                shouldPass = true ;
                return ;
            }
        }

        // * 到达这里说明是一方执行了pass操作后另一方有合法下棋位置
        if(shouldPass)
        {
            // * 说明当前玩家有合法下棋位置，取消pass
            shouldPass = false ;
        }
    }
}
