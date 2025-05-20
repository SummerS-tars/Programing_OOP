package top.thesumst.core.container;

import top.thesumst.core.loop.*;
import top.thesumst.io.provider.*;
import top.thesumst.observer.*;
import top.thesumst.tools.PrintTools;
import top.thesumst.type.Event;

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
        currentGameOrder = 1;
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
            gameLoop.setCurrentGameOrder(currentGameOrder);
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