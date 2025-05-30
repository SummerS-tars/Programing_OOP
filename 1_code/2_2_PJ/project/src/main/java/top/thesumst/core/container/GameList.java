package top.thesumst.core.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

import top.thesumst.core.mode.*;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.ScannerTools;
import top.thesumst.type.ChessStatement;
import top.thesumst.type.exception.*;
import top.thesumst.view.console.CLIPrintTools;

public class GameList
{
    private static int gameNumber ;
    private static List<GameMode> games ;
    private static String player1Name , player2Name ;
    private static ChessStatement player1Color , player2Color ;

    public GameList()
    {
        gameNumber = 0 ;
        games = new ArrayList<GameMode>() ;
        setInitializeInfo() ;
        try {
            addGame("peace") ;
            addGame("reversi") ;
            addGame("gomoku") ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GameList(String player1Name, String player2Name)
    {
        gameNumber = 0 ;
        games = new ArrayList<GameMode>() ;
        GameList.player1Name = player1Name ;
        GameList.player2Name = player2Name ;
        player1Color = ChessStatement.BLACK ;
        player2Color = ChessStatement.WHITE ;
        try {
            addGame("peace") ;
            addGame("reversi") ;
            addGame("gomoku") ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * * addGame方法用于添加游戏
     * @param gameMode 游戏模式:
     * 1. peace: 和平模式
     * 2. reversi: 翻转棋
     * @return boolean 是否成功
     */
    public static boolean addGame(String gameMode) throws IllegalCommandException
    {
        int maxNumber = 8 ;
        if(gameNumber >= maxNumber)
            throw new IllegalCommandException("游戏数量已达上限！") ;

        gameNumber++ ;
        int size = 8 ;
        int gomokuSize = 15 ;
        switch (gameMode) {
            case "peace":
                games.add(new PeaceMode(gameNumber, gameMode, size, player1Name, player2Name, player1Color, player2Color));
                return true;
            case "reversi":
                games.add(new ReversiMode(gameNumber, gameMode, size, player1Name, player2Name, player1Color, player2Color));
                return true;
            case "gomoku":
                games.add(new GomokuMode(gameNumber, gameMode, gomokuSize, player1Name, player2Name, player1Color, player2Color));
                return true;
            default:
                PauseTools.pause("游戏模式错误！");
        }
        return false;
    }

    /**
     * * getGame方法用于获取游戏
     * @param gameNum 游戏编号
     * @return GameMode 游戏
     */
    public static GameMode getGame(int gameNum)
    {
        return games.get(gameNum - 1) ;
    }

    /**
     * * getGameNumber方法用于获取游戏数量
     * @return int 游戏数量
     */
    public static int getGameNumber()
    {
        return gameNumber ;
    }

    /**
     * * setInitializeInfo方法用于设置游戏初始化信息
     * * 包括玩家名称和棋子颜色
     */
    private static void setInitializeInfo()
    {
        try{
            Scanner sc = ScannerTools.getScanner() ;
            CLIPrintTools.clearConsole();
            System.out.println("请输入1号玩家的名称：");
            player1Name = sc.nextLine() ;
            
            boolean optionAvailable = false ;
            while(!optionAvailable)
            {
                CLIPrintTools.clearConsole();
                System.out.println("请选择1号玩家的棋子颜色：(1.Black ○ | 2.White ● )");
                try {
                    int op = sc.nextInt();
                    sc.nextLine(); // 读取缓冲区中多余的换行符
                    switch (op) {
                        case 1:
                            player1Color = ChessStatement.BLACK;
                            optionAvailable = true;
                            break;
                        case 2:
                            player1Color = ChessStatement.WHITE;
                            optionAvailable = true;
                            break;
                        default:
                            PauseTools.pause("输入无效！请输入数字1或2，请键入回车键开始重新选择");
                            break;
                    }
                } catch (InputMismatchException e) {
                    sc.nextLine(); // 清空缓冲区
                    PauseTools.pause("输入无效！请输入数字1或2，请键入回车键开始重新选择");
                }
            }
            CLIPrintTools.clearConsole();
            System.out.println("请输入2号玩家的名称：");
            player2Name = sc.nextLine() ;
        } catch(Exception e)
        {
            System.out.println("输入错误！请重新输入");
            ScannerTools.closeScanner();
            setInitializeInfo() ;
        }

        player2Color = (player1Color == ChessStatement.BLACK) ? ChessStatement.WHITE : ChessStatement.BLACK ;
    }
}
