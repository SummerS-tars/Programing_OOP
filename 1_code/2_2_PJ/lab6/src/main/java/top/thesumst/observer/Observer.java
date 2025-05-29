package top.thesumst.observer;

import top.thesumst.core.container.GameList;
import top.thesumst.type.Event;

public interface Observer 
{
    /**
     * update方法，当被观察对象状态改变时调用此方法
     * @param event
     * @param gameList
     * @param currentGameOrder
     */
    void update(Event event, GameList gameList, int currentGameOrder);

    /**
     * 初始化方法，用于设置观察者的初始状态
     * @param gameList
     * @param currentGameOrder
     */
    void init(GameList gameList, int currentGameOrder);
}
