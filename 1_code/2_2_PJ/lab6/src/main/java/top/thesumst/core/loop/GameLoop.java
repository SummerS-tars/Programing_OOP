package top.thesumst.core.loop;

import java.util.List;

import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.observer.BaseSubject;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public abstract class GameLoop extends BaseSubject
{
    protected final GameList gameList;
    protected final BaseCommandProvider cmdProvider;
    protected boolean isLooping;
    protected int currentGameOrder;

    public GameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        this.registerObserver(observer);
        this.gameList = gameList;
        this.cmdProvider = cmdProvider;
        this.isLooping = false;
    }

    /**
     * 开始游戏循环
     * @return Event
     */
    abstract public Event startLoop();

    abstract public void stopLoop();

    /**
     * 游戏主循环，从CommandProvider中获取命令并执行
     * 返回无法直接在游戏中处理的事件
     * @return
     */
    abstract protected Event gameLoop();

    protected GameMode getCurrentGame()
    {
        return GameList.getGame(currentGameOrder);
    }

    public void setCurrentGameOrder(int currentGameOrder)
    {
        this.currentGameOrder = currentGameOrder;
    }

    public void playback(List<Event> events)
    {
        for(Event event : events)
        {
            event.executeEvent(gameList, currentGameOrder);
            notifyObservers(event, gameList, currentGameOrder);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
