package top.thesumst.core.loop;

import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.observer.Observer;

public class GameLoopFactory 
{
    public static GameLoop getGameLoop(String gameLoopType, GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        if(gameLoopType.equals("cli")) return new CLIGameLoop(gameList, cmdProvider, observer);
        else if(gameLoopType.equals("gui")) return new GUIGameLoop(gameList, cmdProvider, observer);
        else throw new IllegalArgumentException("游戏循环类型错误: " + gameLoopType);
    }
}
