package top.thesumst.core.mode;

import top.thesumst.type.ChessStatement;
import top.thesumst.core.mode.component.*;
import top.thesumst.exception.*;
import top.thesumst.type.*;
import java.awt.Point;
import java.util.Stack;

public abstract class GameMode 
{
    public final int gameOrder ;
    public final String gameMode ;
    public final int size ;
    private ChessBoard board;
    protected Player player1, player2;
    protected boolean isBlackTurn, isOver;
    @SuppressWarnings("unused")
    private Stack<Step> stepStack, undoStack;

    GameMode(int order, String mode,int size, String name1, String name2, ChessStatement color1, ChessStatement color2)
    {
        gameOrder = order;
        gameMode = mode;
        this.size = size;
        board = new ChessBoard(size);
        player1 = new Player(name1, color1);
        player2 = new Player(name2, color2);
        stepStack = new Stack<>();
        undoStack = new Stack<>();
        isBlackTurn = true;
        isOver = false;
    }

    /**
     * receiveOperation方法，接收操作
     * @param operation
     * @return
     * @throws IllegalMoveException
     * @throws IllegalCommandException
     */
    abstract public boolean receiveOperation(Operation<?> operation) 
    throws IllegalMoveException, IllegalCommandException;

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
        return size;
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
        return stepStack.size()+1;
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
