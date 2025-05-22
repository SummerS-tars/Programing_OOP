package top.thesumst.view.console;

import java.awt.Point;

import top.thesumst.core.container.*;
import top.thesumst.core.mode.*;
import top.thesumst.type.Event;
import top.thesumst.type.component.*;

public class CLIPrintTools 
{
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
            goToPoint(getBoardPosition(), i+1);
            System.out.printf("%X ", i + 1) ;
            for(int j = 0 ; j < size ; j ++ )
                System.out.printf("%c " , board.getChessStatement(new Point(i,j)).getSymbol()) ;
        }
    }

    public static void printPlayerInfo(GameMode currentGame)
    {
        Player player1 = currentGame.getPlayer1() ;
        Player player2 = currentGame.getPlayer2() ;
        String infoTitle = String.format("%24s", "");
        String player1Info = String.format("[Player1]%-10s %c", player1.getName(), player1.getColor().getSymbol());
        String player2Info = String.format("[Player2]%-10s %c", player2.getName(), player2.getColor().getSymbol());
        String turnInfo = String.format("当前轮次 : %d" , currentGame.getTurnNumber());
        if(currentGame instanceof ReversiMode)
        {
            player1Info += String.format(" : %-4d", player1.getChessNumber());
            player2Info += String.format(" : %-4d", player2.getChessNumber());
            infoTitle += String.format("%s", "棋子数");
        }
        else if(currentGame instanceof GomokuMode)
        {
            player1Info += String.format(" : %-4d", ((GomokuMode)currentGame).getBombNumber(player1));
            player2Info += String.format(" : %-4d", ((GomokuMode)currentGame).getBombNumber(player2));
            infoTitle += String.format("%s", "炸弹数");
        }
        goToPoint(getPlayerInfoPosition(), -1);
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition(), -1);
        System.out.printf("%s", infoTitle);
        goToPoint(getPlayerInfoPosition());
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition());
        System.out.printf("%s", player1Info);
        goToPoint(getPlayerInfoPosition(), 1);
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition(), 1);
        System.out.printf("%s", player2Info);
        goToPoint(getPlayerInfoPosition(), 2);
        System.out.printf("%30s", "");
        goToPoint(getPlayerInfoPosition(), 2);
        System.out.printf("%s", turnInfo);
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
            String gameString = GameList.getGame(i).toString();
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

    public static void printTipPanel(GameList gameList)
    {
        String inputTips = getTips(gameList) ;
        goToPoint(getTipPosition());
        clearConsoleAfterCursor();
        goToPoint(getTipPosition());
        System.out.printf("%s", inputTips);
    }

    public static void printInputPanel(GameList gameList, Event event)
    {
        GameMode currentGame = GameList.getGame(GameContainer.getCurrentGameOrder()) ;
        String turnInfo = getTurnInfo(currentGame) ;
        goToPoint(getTipPosition());
        if(event != null && event.getMessage() != null)
        {
            goToPoint(getInputPanelPosition(), -1);
            System.out.printf("%s", event.getMessage());
        }
        goToPoint(getInputPanelPosition());
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
        GameMode currentGame = GameList.getGame(GameContainer.getCurrentGameOrder()) ;
        String tips = new String() ;
        String addGameInfo = "增加棋盘(peace/reversi/gomoku)";
        String quitGameInfo = "退出游戏(quit)";
        String passGameInfo = "跳过(pass)";
        String playbackInfo = "演示(playback *.cmd)";
        String switchGameInfo = String.format("切换棋盘[1-%d]", GameList.getGameNumber()) ;
        tips += "请输入命令:\n " ;
        if(currentGame.isOver())
        {
            tips += String.format("1. %s\n ", switchGameInfo);
            tips += String.format("2. %s\n 3. %s", addGameInfo, quitGameInfo);
        }
        else
        {
            tips += String.format("1. 坐标[1A-%X%c](先行后列，支持大小写)\n ", currentGame.getSize(), 'A'+currentGame.getSize()-1);
            tips += String.format("2. %s\n ", switchGameInfo);
            switch(currentGame.getGameMode())
            {
                case "peace":
                case "gomoku":
                    tips += String.format("3. %s\n 4. %s\n 5. %s", addGameInfo, playbackInfo, quitGameInfo);
                    break;
                case "reversi":
                    tips += String.format("3. %s\n 4. %s\n 5. %s\n 6. %s", passGameInfo, addGameInfo, playbackInfo, quitGameInfo);
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
        {    
            turnInfo = "本局游戏已经结束!" ;
            if(currentGame instanceof ReversiMode || currentGame instanceof GomokuMode)
            {
                Player winner ;
                if(currentGame instanceof ReversiMode) winner = ((ReversiMode)currentGame).getWinner() ;
                else if(currentGame instanceof GomokuMode) winner = ((GomokuMode)currentGame).getWinner() ;
                else winner = null ;
                if(winner != null)
                    turnInfo += " 胜者: " + winner.getName() + " " + winner.getColor().getSymbol() ;
                else
                    turnInfo += " 平局!" ;
            }
            turnInfo += "\n请输入指令 : " ;
        }
        else
        {
            Player currentPlayer = currentGame.getCurrentPlayer() ;
            turnInfo = "当前回合: " + currentPlayer.getName() + " " + currentPlayer.getColor().getSymbol() + " : " ;
            if(currentGame instanceof ReversiMode && ((ReversiMode)currentGame).shouldPass())
                turnInfo += "当前玩家无有效位置!请执行pass或其他操作 " ;
        }
        return turnInfo ;
    }

    private static Point getBoardPosition()
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
    private static Point getTipPosition()
    {
        return TerminalOutputPositionSets.tipPosition ;
    }
    private static Point getInputPanelPosition()
    {
        return TerminalOutputPositionSets.inputPosition ;
    }
}

class TerminalOutputPositionSets
{
    static Point boardPosition ;
    static Point playerInfoPosition ;
    static Point gameListPosition ;
    static Point tipPosition ;
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
        int tipRow = 1 + size + 2 ;
        int tipCol = 1 ;
        int inputRow = tipRow + 8 ;
        int inputCol = 1 ;

        playerInfoPosition = new Point(playerInfoStartRow, playerInfoStartCol) ;
        gameListPosition = new Point(gameListStartRow, gameListStartCol) ;
        tipPosition = new Point(tipRow, tipCol) ;
        inputPosition = new Point(inputRow, inputCol) ;
    }
}
