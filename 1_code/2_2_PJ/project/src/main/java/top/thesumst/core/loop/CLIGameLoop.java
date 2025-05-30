package top.thesumst.core.loop;

import top.thesumst.core.command.PlaybackCommand;
import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.*;
import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public class CLIGameLoop extends GameLoop
{
    public CLIGameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        super(gameList, cmdProvider, observer);
    }

    @Override
    public void startLoop()
    {
        isRunning = true;
        cmdProvider.open();
        gameLoop();
    }

    @Override
    public void gameLoop()
    {
        Event event = null;
        while(isRunning)
        {
            event = null;
            cmdProvider.getNextCommand();
            event = cmdProvider.getEvent();
            event.executeEvent(gameList, currentGameOrder);
            notifyObservers(event, gameList, currentGameOrder);
            if(event.getCommand() instanceof PlaybackCommand)
            {
                playback(event);
            }
        }
    }

    @Override
    public void stopLoop()
    {
        isRunning = false;
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