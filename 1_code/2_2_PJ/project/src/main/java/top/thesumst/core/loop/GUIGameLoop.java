package top.thesumst.core.loop;

import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.observer.Observer;

public class GUIGameLoop extends GameLoop
{
    // TODO: 具体GUI功能待实现
    public GUIGameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        super(gameList, cmdProvider, observer);
    }

    @Override
    public void startLoop()
    {
    }

    @Override
    public void stopLoop()
    {
    }


    @Override    
    public void gameLoop()
    {
    }
}