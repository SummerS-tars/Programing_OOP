package top.thesumst.core.loop;

import top.thesumst.core.command.CommandResult;
import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.*;
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
            processEvent(event);

            if(event.getState() == EventState.EVENT_EXECUTED_SUCCESS || 
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

    private void processEvent(Event event)
    {
        if(event.getType() == EventType.GAME_OPERATION)
        {
            CommandResult result = event.getCommand().execute(getCurrentGame(), gameList);
            if(result.isSuccess()) event.setState(EventState.EVENT_EXECUTED_SUCCESS);
            else event.setState(EventState.EVENT_EXECUTED_FAIL);
            event.setMessage(result.getMessage());
        }
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
    }

    public static void main(String[] args)
    {
        // 创建一个CLICommandProvider实例
        BaseCommandProvider commandProvider = new CLICommandProvider();
        // 创建一个GameList实例
        GameList gameList = new GameList("test1", "test2");
        // 创建一个Observer实例
        Observer observer = new ObserverTest();
        // 创建一个CLIGameLoop实例
        CLIGameLoop cliGameLoop = new CLIGameLoop(gameList, commandProvider, observer);
        // 启动游戏
        Event event = cliGameLoop.startLoop();
        System.out.println("Game loop ended with event: " + event);
    }
}