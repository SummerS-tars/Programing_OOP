package top.thesumst.core.container;

import java.util.ArrayList;
import top.thesumst.core.loop.*;
import top.thesumst.core.mode.GameMode;
import top.thesumst.io.input.InputParser;
import top.thesumst.io.provider.*;
import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;
import top.thesumst.observer.*;
import top.thesumst.type.Event;
import top.thesumst.view.console.CLIPrintTools;

public class GameContainer extends BaseSubject
{
    // * 此处默认以及一个程序只会有一个容器运行，且只有一个游戏列表
    private static GameList gameList ;
    private static GameLoop gameLoop ;
    private static int currentGameOrder ;
    private static GameContainer currentInstance;

    public GameContainer(GameList gameList, GameLoop gameLoop, BaseCommandProvider commandProvider, Observer observer)
    {
        GameContainer.gameList = gameList;
        GameContainer.gameLoop = gameLoop;
        GameContainer.currentInstance = this;
        registerObserver(observer);
        switchGameOrder(1);
        notifyInit(gameList, currentGameOrder);
    }

    /**
     * ! 游戏主循环
     */
    public void runGame()
    {
        gameLoop.startLoop();
    }

    /**
     * 切换游戏
     * @param order
     */
    public static void switchGameOrder(int order)
    {
        // 验证游戏列表是否已正确初始化
        GameList.validateGamesInitialized();

        currentGameOrder = order;
        gameLoop.setCurrentGameOrder(currentGameOrder);
        
        // 再次检查游戏是否存在
        try {
            InputParser.setBoardSize(GameList.getGame(currentGameOrder).size);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("警告: 无法找到序号为 " + currentGameOrder + " 的游戏。设置为默认值1。");
            currentGameOrder = 1;
            gameLoop.setCurrentGameOrder(currentGameOrder);
            // 尝试再次获取
            try {
                InputParser.setBoardSize(GameList.getGame(currentGameOrder).size);
            } catch (Exception ex) {
                System.err.println("严重错误: 无法初始化游戏。");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取当前游戏序号
     * @return 当前游戏序号
     */
    public static int getCurrentGameOrder()
    {
        return currentGameOrder;
    }    public static GameMode getCurrentGame()
    {
        return GameList.getGame(currentGameOrder);
    }
    
    /**
     * 获取游戏列表（用于序列化）
     */
    public static GameList getGameList() {
        return gameList;
    }
    
    /**
     * 获取游戏循环（用于序列化）
     */
    public static GameLoop getGameLoop() {
        return gameLoop;
    }

    /**
     * 设置游戏列表（用于反序列化）
     * @param newGameList 新的游戏列表
     */
    public static void setGameList(GameList newGameList) {
        gameList = newGameList;
    }
    
    /**
     * 设置游戏循环（用于反序列化）
     * @param newGameLoop 新的游戏循环
     */
    public static void setGameLoop(GameLoop newGameLoop) {
        gameLoop = newGameLoop;
    }
    
    /**
     * 获取当前GameContainer实例
     * @return 当前实例
     */
    public static GameContainer getCurrentInstance() {
        return currentInstance;
    }
    
    /**
     * 设置当前游戏序号（用于反序列化）
     * @param order 游戏序号
     */
    public static void setCurrentGameOrder(int order) {
        currentGameOrder = order;
        if (gameLoop != null) {
            gameLoop.setCurrentGameOrder(currentGameOrder);
        }
        if (gameList != null && GameList.getGame(currentGameOrder) != null) {
            InputParser.setBoardSize(GameList.getGame(currentGameOrder).size);
        }
    }

    public static void stopGame()
    {
        gameLoop.stopLoop();
    }
}

class GameContainerCLITestDrive
{
    static class ObserverTest implements Observer
    {
        @Override
        public void update(Event event, GameList gameList, int currentGameOrder)
        {
            CLIPrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
            CLIPrintTools.clearConsole();
            CLIPrintTools.printBoard(GameList.getGame(currentGameOrder));
            CLIPrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
            CLIPrintTools.printGameList(gameList);
            CLIPrintTools.printTipPanel(gameList);
            CLIPrintTools.printInputPanel(gameList, event);
            System.out.println("Observer notified: " + event);
        }

        @Override
        public void init(GameList gameList, int currentGameOrder)
        {
            CLIPrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
            CLIPrintTools.clearConsole();
            CLIPrintTools.printBoard(GameList.getGame(currentGameOrder));
            CLIPrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
            CLIPrintTools.printGameList(gameList);
            CLIPrintTools.printTipPanel(gameList);
            CLIPrintTools.printInputPanel(gameList, null);
            System.out.println("Observer initialized with game list: " + gameList + " and current game order: " + currentGameOrder);
        }
    }

    public static void main(String[] args) 
    {
        GameList gameList = new GameList();
        BaseCommandProvider cmdProvider = new CLICommandProvider(CommandProviderMode.CLI);
        Observer observer = new ObserverTest();
        GameLoop gameLoop = new CLIGameLoop(gameList, cmdProvider, observer);
        GameContainer gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
        gameContainer.runGame();
    }
}