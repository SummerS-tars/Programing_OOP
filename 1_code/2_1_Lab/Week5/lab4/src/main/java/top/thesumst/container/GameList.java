package top.thesumst.container;

import top.thesumst.mode.GameMode;
import top.thesumst.mode.PeaceMode;
import top.thesumst.mode.ReversiMode;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.PrintTools;
import top.thesumst.type.ChessColor;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;

public class GameList
{
    private static int gameNumber ;
    private List<GameMode> games ;
    private static String player1Name , player2Name ;
    private static ChessColor player1Color , player2Color ;

    public GameList()
    {
        gameNumber = 0 ;
        games = new ArrayList<GameMode>() ;
        setInitializeInfo() ;
        addGame("peace") ;
        addGame("reversi") ;
    }    

    /**
     * * addGame方法用于添加游戏
     * @param gameMode 游戏模式:
     * 1. peace: 和平模式
     * 2. reversi: 翻转棋
     * @return boolean 是否成功
     */
    public boolean addGame(String gameMode)
    {
        int maxNumber = 8 ;
        if(gameNumber >= maxNumber)
        {
            return false ;
        }

        gameNumber++ ;
        int size = 8 ;
        switch (gameMode) {
            case "peace":
                games.add(new PeaceMode(gameNumber, gameMode, size, player1Name, player2Name, player1Color, player2Color));
                return true;
            case "reversi":
                games.add(new ReversiMode(gameNumber, gameMode, size, player1Name, player2Name, player1Color, player2Color));
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
    public GameMode getGame(int gameNum)
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
    private void setInitializeInfo()
    {
        PrintTools printTools = new PrintTools();
        PrintTools.clearConsole(); 
        System.out.println("请输入1号玩家的名称：");
        player1Name = printTools.sc.nextLine() ;
        
        boolean optionAvailable = false ;
        while(!optionAvailable)
        {
            PrintTools.clearConsole();
            System.out.println("请选择1号玩家的棋子颜色：(1.Black ○ | 2.White ● )");
            try {
                int op = printTools.sc.nextInt();
                printTools.sc.nextLine(); // 读取缓冲区中多余的换行符
                switch (op) {
                    case 1:
                        player1Color = ChessColor.BLACK;
                        optionAvailable = true;
                        break;
                    case 2:
                        player1Color = ChessColor.WHITE;
                        optionAvailable = true;
                        break;
                    default:
                        PauseTools.pause("输入无效！请输入数字1或2，请键入回车键开始重新选择");
                        break;
                }
            } catch (InputMismatchException e) {
                printTools.sc.nextLine(); // 清空缓冲区
                PauseTools.pause("输入无效！请输入数字1或2，请键入回车键开始重新选择");
            }
        }

        PrintTools.clearConsole();
        System.out.println("请输入2号玩家的名称：");
        player2Name = printTools.sc.nextLine() ;

        player2Color = (player1Color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK ;
    }

    public static void main(String[] args) 
    {
        GameList gameList = new GameList() ;
        PrintTools.clearConsole();

        // 测试初始化信息输入
        System.out.println(player1Name + " " + player1Color);
        System.out.println(player2Name + " " + player2Color);
        System.out.println(GameList.getGameNumber());
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();

        // 测试游戏添加
        GameMode game ;
        game = gameList.getGame(1) ;
        System.out.println(game);
        game.printBoard();
        game.printPlayerInfo();
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();

        game = gameList.getGame(2) ;
        System.out.println(game);
        game.printBoard();
        game.printPlayerInfo();
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();

        // 测试额外添加
        gameList.addGame("peace") ;
        game = gameList.getGame(3) ;
        System.out.println("After add peace, gameNumber : " + GameList.getGameNumber());
        System.out.println(game);
        game.printBoard();
        game.printPlayerInfo();
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();

        gameList.addGame("reversi") ;
        game = gameList.getGame(4) ;
        System.out.println("After add peace, gameNumber : " + GameList.getGameNumber());
        System.out.println(game);
        game.printBoard();
        game.printPlayerInfo();
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();

        PauseTools.pause("测试完毕，请按回车键以退出");
        return ;
    }
}
