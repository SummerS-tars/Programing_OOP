package top.thesumst.core.container;

import top.thesumst.core.loop.*;
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
    private static boolean isRunning ;

    public GameContainer(GameList gameList, GameLoop gameLoop, BaseCommandProvider commandProvider, Observer observer)
    {
        GameContainer.gameList = gameList;
        GameContainer.gameLoop = gameLoop;
        registerObserver(observer);
        switchGameOrder(1);
        isRunning = true;
        notifyInit(gameList, currentGameOrder);
    }

    /**
     * ! 游戏主循环
     */
    public void runGame()
    {
        Event event = null;
        while(isRunning)
        {
            event = null;
            event = gameLoop.startLoop();
            event.executeEvent(gameList, currentGameOrder);
            notifyObservers(event, gameList, currentGameOrder);
        }
    }

    /**
     * 切换游戏
     * @param order
     */
    public static void switchGameOrder(int order)
    {
        currentGameOrder = order;
        gameLoop.setCurrentGameOrder(currentGameOrder);
        InputParser.setBoardSize(GameList.getGame(currentGameOrder).size);
    }

    /**
     * 获取当前游戏序号
     * @return 当前游戏序号
     */
    public static int getCurrentGameOrder()
    {
        return currentGameOrder;
    }

    public static void stopGame()
    {
        isRunning = false ;
    }
}

class GameContainerTestDrive
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