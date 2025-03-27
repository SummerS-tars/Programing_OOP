package top.thesumst.tools;

import top.thesumst.mode.GameMode;
import top.thesumst.mode.component.*;
import top.thesumst.container.*;
import java.util.Scanner;
import java.awt.Point;

public class PrintTools 
{
    public Scanner sc = new Scanner(System.in) ;

    public static void initializePositionsSet(GameMode currentGame)
    {
        TerminalOutputPositionSets.setPositions(currentGame);
    }

    public static void printBoard(GameMode currentGame)
    {
        int size = currentGame.getSize() ;
        ChessBoard board = currentGame.getBoard() ;
        System.out.printf("%3s", "");
        for(int i = 0 ; i < size ; i ++ )
            System.out.printf("%c ", 'A' + i) ;
        for(int i = 0 ; i < size ; i ++ )
        {
            goToPoint(getBoartPosition(), i+1);
            System.out.printf("%2d ", i + 1) ;
            for(int j = 0 ; j < size ; j ++ )
                System.out.printf("%c " , board.getChessColor(new Point(i,j)).getSymbol()) ;
        }
    }

    public static void printPlayerInfo(GameMode currentGame)
    {
        Player player1 = currentGame.getPlayer1() ;
        Player player2 = currentGame.getPlayer2() ;
        String player1Info = String.format("[Player1]%-10s %c", player1.getName(), player1.getColor().getSymbol());
        String player2Info = String.format("[Player2]%-10s %c", player2.getName(), player2.getColor().getSymbol());
        if(currentGame.getGameMode().equals("reversi"))
        {
            player1Info += String.format(" : %-4d", player1.getChessNumber());
            player2Info += String.format(" : %-4d", player2.getChessNumber());
        }
        goToPoint(getPlayerInfoPosition());
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition());
        System.out.printf("%s", player1Info);
        goToPoint(getPlayerInfoPosition(), 1);
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition(), 1);
        System.out.printf("%s", player2Info);
    }

    public static void printGameList(GameList gameList)
    {
        goToPoint(getGameListPosition());
        String blankSide = "=".repeat(26) ;
        System.out.printf("%s\n", blankSide);
        String[] lists = new String[4] ;
        for(int i = 0 ; i < 4 ; i++)
            lists[i] = "" ;
        for(int i = 1 ; i <= GameList.getGameNumber() ; i++)
        {
            String preString = i == GameContainer.getCurrentGameOrder() ? "> " : "  ";
            String gameString = gameList.getGame(i).toString();
            gameString = String.format("%-10s", gameString);
            lists[(i - 1) % 4] += preString + gameString ;
        }
        for(int i = 0 ; i < 4 && lists[i] != null ; i++)
        {
            goToPoint(getGameListPosition(), i+1);
            System.out.printf("%s", lists[i]);
        }
        goToPoint(getGameListPosition(), 5);
        System.out.printf("%s\n", blankSide);
    }

    public static void printInputPanel(GameList gameList)
    {
        GameMode currentGame = gameList.getGame(GameContainer.getCurrentGameOrder()) ;
        String turnInfo = getTurnInfo(currentGame) ;
        String inputTips = getTips(gameList) ;
        goToPoint(getInputPosition());
        clearConsoleAfterCursor();
        goToPoint(getInputPosition(), 5);
        System.out.printf("%s", inputTips);
        goToPoint(getInputPosition());
        System.out.printf("%s", turnInfo);
    }

    /**
     * * clearConsole方法用于清空控制台
     */
    public static void clearConsole()
    {
        /**
         * * 转义字符说明：
         * * \033 ESC (ASCII 27)
         * * [H 光标位置命令，将光标移回屏幕左上角（行1，列1）
         * * [2J 清屏
         * 
         * * System.out.flush() 用于确保输出生效
         */
        System.out.print("\033[H\033[2J") ;
        System.out.flush();
    }

    /**
     * * rememberCursor方法用于记录当前光标位置
     */
    public static void rememberCursor()
    {
        System.out.print("\033[s") ;
        System.out.flush();
    }

    /**
     * * restoreCursor方法用于恢复光标位置
     */
    public static void restoreCursor()
    {
        System.out.print("\033[u") ;
        System.out.flush();
    }

    /**
     * * clearConsoleAfterCursor方法用于清除光标位置之后的内容
     */
    public static void clearConsoleAfterCursor()
    {
        System.out.print("\033[J") ;
        System.out.flush();
    }

    public static void goToResultPosition()
    {
        goToPoint(getInputPosition(), 1);
    }

    private static void goToPoint(Point p)
    {
        String output = "\033[" + p.x + ";" + p.y + "H" ;
        System.out.print(output) ;
        System.out.flush();
    }

    private static void goToPoint(Point p, int bias)
    {
        String output = "\033[" + (p.x + bias) + ";" + p.y + "H" ;
        System.out.print(output) ;
        System.out.flush();
    }

    /**
     * * getTips方法，获取提示信息
     * @param gameList
     * @return tips 提示信息
     */
    private static String getTips(GameList gameList)
    {
        GameMode currentGame = gameList.getGame(GameContainer.getCurrentGameOrder()) ;
        String tips = new String() ;
        tips += "请输入命令:\n " ;
        if(currentGame.isOver())   //未结束
        {
            tips += "1. 切换棋盘[1-" + GameList.getGameNumber() + "]\n ";
            tips += "2. 增加棋盘(peace/reversi)\n 3. 退出游戏(quit)";
        }
        else
        {
            tips += "1. 坐标[1A-" + currentGame.getSize() + (char)('A'+currentGame.getSize()-1) +"](支持大小写+乱序)\n ";
            tips += "2. 切换棋盘[1-" + GameList.getGameNumber() + "]\n ";
            switch(currentGame.getGameMode())
            {
                case "peace":
                    tips += "3. 增加棋盘(peace/reversi)\n 4. 退出游戏(quit)";
                    break;
                case "reversi":
                    tips += "3. 跳过(pass)\n 4. 增加棋盘(peace/reversi)\n 5. 退出游戏(quit)";
                    break;
            }
        }
        return tips;
    }

    /**
     * * getTurnInfo方法，获取回合信息
     * @param currentGame
     * @return turnInfo 回合信息
     */
    private static String getTurnInfo(GameMode currentGame)
    {
        String turnInfo ;
        if(currentGame.isOver())
            turnInfo = "本局游戏已经结束，请输入其他操作！" ;
        else
        {
            Player currentPlayer = currentGame.getCurrentPlayer() ;
            turnInfo = "当前回合: " + currentPlayer.getName() + " " + currentPlayer.getColor().getSymbol() + " : " ;
        }
        return turnInfo ;
    }

    private static Point getBoartPosition()
    {
        return TerminalOutputPositionSets.boardPosition ;
    }
    private static Point getPlayerInfoPosition()
    {
        return TerminalOutputPositionSets.playerInfoPosition ;
    }
    private static Point getGameListPosition()
    {
        return TerminalOutputPositionSets.gameListPosition ;
    }
    private static Point getInputPosition()
    {
        return TerminalOutputPositionSets.inputPosition ;
    }
}

class TerminalOutputPositionSets
{
    static Point boardPosition ;
    static Point playerInfoPosition ;
    static Point gameListPosition ;
    static Point inputPosition ;

    static
    {
        boardPosition = new Point(1,1) ;
    }

    static void setPositions(GameMode currentGame)
    {
        int size = currentGame.getSize() ;
        int playerInfoStartRow = 1 + size / 2;
        int playerInfoStartCol = 3 + 2 * size + 4 ;
        int gameListStartRow = playerInfoStartRow - 2 ;
        int gameListStartCol = playerInfoStartCol + 40 ;
        int inputRow = 1 + size + 2 ;
        int inputCol = 1 ;

        playerInfoPosition = new Point(playerInfoStartRow, playerInfoStartCol) ;
        gameListPosition = new Point(gameListStartRow, gameListStartCol) ;
        inputPosition = new Point(inputRow, inputCol) ;
    }
}
