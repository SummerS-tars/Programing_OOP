package top.thesumst.mode;

import top.thesumst.mode.component.*;
import top.thesumst.type.ChessStatement;
import top.thesumst.exception.*;
import java.awt.Point;
import java.util.Stack;

public abstract class GameMode 
{
    final int gameOrder ;
    final String gameMode ;
    final int maxSize ;
    private ChessBoard board;
    protected Player player1, player2;
    protected boolean isBlackTurn, isOver;
    @SuppressWarnings("unused")
    private Stack<Step> stepStack, undoStack;

    GameMode(int order, String mode,int size, String name1, String name2, ChessStatement color1, ChessStatement color2)
    {
        gameOrder = order;
        gameMode = mode;
        maxSize = size;
        board = new ChessBoard(maxSize);
        player1 = new Player(name1, color1);
        player2 = new Player(name2, color2);
        stepStack = new Stack<>();
        undoStack = new Stack<>();
        isBlackTurn = true;
        isOver = false;
    }

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param Point 下棋位置
     * @return boolean 是否成功
     * @throws IllegalMoveException
     */
    public abstract boolean receiveOperation(Point point) 
    throws IllegalMoveException;

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param String 操作
     * 应该是pass或者quit，其中pass只有在ReversiMode中有用
     * @return boolean 是否成功
     * @throws IllegalCommandException
     */
    public abstract boolean receiveOperation(String operation) 
    throws IllegalCommandException ;

    // /**
    //  * * rollback方法，回退一步
    //  * @return
    //  */
    // protected abstract boolean rollback() ;
    // to be added

    /**
     * * toString方法，返回游戏模式信息
     * @return String 游戏模式信息  
     */
    @Override
    public String toString() 
    {
        return gameOrder + ". " + gameMode;
    }

    /**
     * * getters and setters
     */
    public int getSize()
    {
        return maxSize;
    }
    public String getGameMode()
    {
        return gameMode;
    }
    public boolean isOver()
    {
        return isOver;
    }
    public boolean isBlackTurn()
    {
        return isBlackTurn;
    }
    public Player getPlayer1()
    {
        return player1;
    }
    public Player getPlayer2()
    {
        return player2;
    }
    public Player getCurrentPlayer()
    {
        return (player1.getColor() == (isBlackTurn ? ChessStatement.BLACK : ChessStatement.WHITE )) ? player1 : player2;
    }
    public ChessBoard getBoard()
    {
        return board;
    }
    public int getTurnNumber()
    {
        return stepStack.size();
    }
    protected ChessStatement getChessStatement(Point point)
    {
        return board.getChessStatement(point);
    }
    protected void setChessColor(Point point, ChessStatement color)
    {
        board.setChessStatement(point, color);
    }
    protected int getChessNumber(ChessStatement color)
    {
        return board.getChessNumber(color);
    }
    protected void addStep(Step step)
    {
        stepStack.push(step);
    }
}
