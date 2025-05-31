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

    // Flag to indicate if deserialization is in progress
    // transient to ensure it's not part of serialization itself
    private static transient boolean isDeserializing = false;
    
    // Flag to indicate that this instance was created from deserialization
    // and should be treated as such
    private transient boolean isFromDeserialization = false;

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
     * 专门用于反序列化的构造函数
     * 不会重新初始化任何游戏，而是使用已经设置的静态数据
     * @param fromDeserialization 标记这是从反序列化创建的实例
     */
    public GameList(boolean fromDeserialization) {
        if (fromDeserialization) {
            this.isFromDeserialization = true;
            // 不初始化任何内容，假设静态字段已由反序列化器填充
            System.out.println("从反序列化创建GameList实例，包含 " + games.size() + " 个游戏");
        } else {
            throw new IllegalArgumentException("这个构造函数只应该由反序列化器调用，fromDeserialization参数必须为true");
        }
    }
    
    /**
     * 检查游戏列表是否已初始化
     * 如果未初始化或为空，则创建默认游戏
     * @return 如果游戏列表已正确初始化则返回true
     */
    public static boolean validateGamesInitialized() {
        // 如果正在反序列化，不要重新初始化游戏
        if (isDeserializing) {
            System.out.println("正在反序列化，跳过游戏列表初始化检查");
            return true;
        }
        
        if (games == null || games.isEmpty()) {
            System.out.println("警告: 游戏列表为空或未初始化，创建默认游戏。");
            try {
                if (games == null) {
                    games = new ArrayList<>();
                    gameNumber = 0;
                }
                
                // 确保玩家名称已设置，如果没有则使用默认值
                if (player1Name == null) player1Name = "玩家1";
                if (player2Name == null) player2Name = "玩家2";
                
                // 确保棋子颜色已设置
                if (player1Color == null) player1Color = ChessStatement.BLACK;
                if (player2Color == null) player2Color = ChessStatement.WHITE;
                
                // 添加默认游戏
                addGame("peace");
                addGame("reversi");
                addGame("gomoku");
                return true;
            } catch (Exception e) {
                System.err.println("创建默认游戏失败: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // --- Static Getters ---
    public static int getGameNumber()
    {
        return gameNumber ;
    }

    public static List<GameMode> getGames() {
        return games;
    }

    public static String getPlayer1Name() {
        return player1Name;
    }

    public static String getPlayer2Name() {
        return player2Name;
    }

    public static ChessStatement getPlayer1Color() {
        return player1Color;
    }

    public static ChessStatement getPlayer2Color() {
        return player2Color;
    }

    // --- Static Setters ---
    public static void setGameNumber(int gameNum) {
        gameNumber = gameNum;
    }

    public static void setGames(List<GameMode> gameList) {
        games = gameList;
    }

    public static void setPlayer1Name(String name) {
        player1Name = name;
    }

    public static void setPlayer2Name(String name) {
        player2Name = name;
    }

    public static void setPlayer1Color(ChessStatement color) {
        player1Color = color;
    }

    public static void setPlayer2Color(ChessStatement color) {
        player2Color = color;
    }
    
    // --- Deserialization lifecycle methods ---
    public static void startDeserialization() {
        isDeserializing = true;
        // Initialize games list if null to avoid NPE during deserializer adding games
        if (games == null) {
            games = new ArrayList<>();
        }
    }

    public static void endDeserialization() {
        isDeserializing = false;
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
