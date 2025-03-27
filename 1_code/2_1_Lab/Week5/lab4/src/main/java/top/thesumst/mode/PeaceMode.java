package top.thesumst.mode;

import top.thesumst.type.ChessColor;
import top.thesumst.mode.component.Player;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.PrintTools;
import java.awt.Point;

public class PeaceMode extends GameMode
{
    public PeaceMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2)
    {
        super(order, mode, size, name1, name2, color1, color2);
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
            System.out.println("游戏已结束");
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
                System.out.println("quit成功，游戏即将结束");
                return true;
            case "pass":
                System.out.println("和平模式下无法进行pass操作");
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
            ChessColor putColor = isBlackTurn ? player1.getColor() : player2.getColor() ;
            board.setChessColor(point, putColor);
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
        if(board.getChessColor(point) == ChessColor.BLANK)
        {
            return true ;
        }
        System.out.println("输入无效，当前位置已有棋子");
        return false ;
    }

    /**
     * * checkGameOver方法，检查游戏是否结束
     * @return true 游戏结束 false 游戏未结束
     */
    private boolean checkGameOver()
    {
        int blackChess = board.getChessNumber(ChessColor.BLACK) ;
        int whiteChess = board.getChessNumber(ChessColor.WHITE) ;
        if(blackChess + whiteChess == maxSize * maxSize) return true ;
        return false ;
    }

    /**
     * test output
     */
    private String getPlayerName(Player p)
    {
        return p.getName();
    }
    private ChessColor getPlayerChessColor(Player p)
    {
        return p.getColor();
    }
    // private void printBoard()
    // {
    //     for(int i = 0; i < maxSize; i++)
    //     {
    //         for(int j = 0; j < maxSize; j++)
    //             System.out.print(board.getChessColor(new Point(i, j)).getSymbol()+" ") ;
    //         System.out.println();
    //     }     
    // }

    /**
     * * main方法用于测试
     * @param args
     */
    public static void main(String[] args) 
    {
        PeaceMode peace = new PeaceMode(1, "peace", 8, 
                    "test1", "test2", ChessColor.BLACK, ChessColor.WHITE) ;

        System.out.printf("player1: name = %s, color = %c\nplayer2: name = %s, color = %c\n",
                        peace.getPlayerName(peace.player1), peace.getPlayerChessColor(peace.player1).getSymbol(),
                        peace.getPlayerName(peace.player2), peace.getPlayerChessColor(peace.player2).getSymbol());
        peace.printBoard();
        System.out.println(peace);
        PauseTools.pause();

        peace.receiveOperation("pass");
        peace.receiveOperation("test");
        PauseTools.pause();

        for(int i = 0 ; i < peace.maxSize ; i++ ) 
        {
            for(int j = 0 ; j < peace.maxSize ; j++ )
            {
                PrintTools.clearConsole();
                peace.receiveOperation(new Point(i, j));
                peace.printBoard();
                PauseTools.pause();
            }
        }

        PrintTools.clearConsole();
        System.out.println("isOver = " + peace.isOver);
        PauseTools.pause("游戏结束，按回车键退出");
        peace.receiveOperation("quit");
    }
}