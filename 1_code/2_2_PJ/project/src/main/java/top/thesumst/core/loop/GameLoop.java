package top.thesumst.core.loop;

import java.io.IOException;
import java.util.List;

import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.io.provider.PlaybackCommandProvider;
import top.thesumst.observer.BaseSubject;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public abstract class GameLoop extends BaseSubject
{
    protected final GameList gameList;
    protected final BaseCommandProvider cmdProvider;
    protected boolean isRunning;
    protected int currentGameOrder;

    public GameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        this.registerObserver(observer);
        this.gameList = gameList;
        this.cmdProvider = cmdProvider;
        this.isRunning = false;
    }

    /**
     * 开始游戏循环
     */
    abstract public void startLoop();

    abstract public void stopLoop();

    /**
     * 游戏主循环，从CommandProvider中获取命令并执行
     * 返回无法直接在游戏中处理的事件
     * @return
     */
    abstract protected void gameLoop();

    protected GameMode getCurrentGame()
    {
        return GameList.getGame(currentGameOrder);
    }

    public void setCurrentGameOrder(int currentGameOrder)
    {
        this.currentGameOrder = currentGameOrder;
    }

    public void playback(Event playbackEvent)
    {
        List<Event> events = null ;
        try {
            PlaybackCommandProvider playbackCommandProvider = new PlaybackCommandProvider(playbackEvent.getData());
            events = playbackCommandProvider.getEvents();
            
            for(Event event : events)
            {
                event.executeEvent(gameList, currentGameOrder);
                notifyObservers(event, gameList, currentGameOrder);
                Thread.sleep(500);
            }
        } catch (IOException e) {
            notifyObservers(Event.getErrorEvent(e), gameList, currentGameOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
