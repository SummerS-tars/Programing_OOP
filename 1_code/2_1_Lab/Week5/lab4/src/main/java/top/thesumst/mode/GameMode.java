package top.thesumst.mode;

import top.thesumst.mode.component.ChessBoard;
import top.thesumst.mode.component.Player;
import top.thesumst.type.ChessColor;
import java.awt.Point;

public abstract class GameMode 
{
    final int gameOrder ;
    final String gameMode ;
    final int maxSize ;
    protected ChessBoard board;
    protected Player player1, player2;
    protected boolean isBlackTurn, isOver;

    GameMode(int order, String mode,int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        gameOrder = order;
        gameMode = mode;
        maxSize = size;
        board = new ChessBoard(maxSize);
        player1 = new Player(name1, color1);
        player2 = new Player(name2, color2);
        isBlackTurn = true;
        isOver = false;
    }

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param Point 下棋位置
     * @return boolean 是否成功
     */
    public abstract boolean receiveOperation(Point point) ;

    /**
     * * receiveOperation方法，接受下棋操作，在输入前就过滤超出棋盘范围的操作
     * @param String 操作
     * 应该是pass或者quit，其中pass只有在ReversiMode中有用
     * @return boolean 是否成功
     */
    public abstract boolean receiveOperation(String operation) ;

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
        return (player1.getColor() == (isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE )) ? player1 : player2;
    }
    public ChessBoard getBoard()
    {
        return board;
    }

    /**
     * // deprecated 现只用于测试
     * * printPlayerInfo方法，打印玩家信息
     * * 不同游戏模式有不同的打印方式
     * ! 将来可能会该改变
     * ! TODO 优化显示方式
     */
    public void printPlayerInfo()
    {
        System.out.printf("[Player1]%-10s %c : %-4d\n", player1.getName(), player1.getColor().getSymbol(), player1.getChessNumber());
        System.out.printf("[Player2]%-10s %c : %-4d\n", player2.getName(), player2.getColor().getSymbol(), player2.getChessNumber());
    }

    /**
     * // deprecated 现只用于测试
     * * printBoard方法，打印棋盘
     * ! 将来可能会该改变
     * ! TODO 优化显示方式
     */
    public void printBoard()
    {
        System.out.printf("%3s", "");
        for(int i = 0 ; i < maxSize ; i ++ )
            System.out.printf("%c ", 'A' + i) ;
        System.out.println() ;
        for(int i = 0 ; i < maxSize ; i ++ )
        {
            System.out.printf("%2d ", i + 1) ;
            for(int j = 0 ; j < maxSize ; j ++ )
            {
                System.out.printf("%c " , board.getChessColor(new Point(i,j)).getSymbol()) ;
            }
            System.out.println() ;
        }
    }
}
