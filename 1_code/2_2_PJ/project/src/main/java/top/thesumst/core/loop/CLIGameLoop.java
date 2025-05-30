package top.thesumst.core.loop;

import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.*;
import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;
import top.thesumst.type.EventState;
import top.thesumst.type.EventType;

public class CLIGameLoop extends GameLoop
{
    public CLIGameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        super(gameList, cmdProvider, observer);
    }

    @Override
    public Event startLoop()
    {
        isLooping = true;
        cmdProvider.open();
        return gameLoop(); // 返回无法直接在游戏中处理的事件
    }

    @Override
    public Event gameLoop()
    {
        Event event = null;
        while(isLooping)
        {
            event = null;
            cmdProvider.getNextCommand();
            event = cmdProvider.getEvent();
            if(event.getType() == EventType.GAME_OPERATION) 
                event.executeEvent(gameList, currentGameOrder);

            if(event.getState() == EventState.EVENT_EXECUTED_SUCCESS || // event被执行过
                event.getState() == EventState.EVENT_EXECUTED_FAIL)
            {
                notifyObservers(event, gameList, currentGameOrder);
                continue;
            }
            else stopLoop();
        }
        return event;
    }

    @Override
    public void stopLoop()
    {
        isLooping = false;
        cmdProvider.close();
    }
}

class CLIGameLoopTestDrive
{
    static class ObserverTest implements Observer
    {
        @Override
        public void update(Event event, GameList gameList, int currentGameOrder)
        {
            System.out.println("Observer notified: " + event);
        }

        @Override
        public void init(GameList gameList, int currentGameOrder)
        {
            System.out.println("Observer initialized with game list: " + gameList + " and current game order: " + currentGameOrder);
        }
    }

    public static void main(String[] args)
    {
        // 创建一个CLICommandProvider实例
        BaseCommandProvider commandProvider = new CLICommandProvider(CommandProviderMode.CLI);
        // 创建一个GameList实例
        GameList gameList = new GameList("test1", "test2");
        // 创建一个Observer实例
        Observer observer = new ObserverTest();
        // 创建一个CLIGameLoop实例
        CLIGameLoop cliGameLoop = new CLIGameLoop(gameList, commandProvider, observer);
        cliGameLoop.setCurrentGameOrder(1);
        // 启动游戏
        cliGameLoop.startLoop();
        // ! 此处只支持普通的下棋操作测试，不支持换棋盘
    }
}