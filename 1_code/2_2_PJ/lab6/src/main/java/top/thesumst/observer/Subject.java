package top.thesumst.observer;

import top.thesumst.core.container.GameList;
import top.thesumst.type.Event;

public interface Subject 
{
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    /**
     * 通知所有观察者
     * @param event
     * @param args 预定：1位为GameList，2位为currentGameOrder
     */
    void notifyObservers(Event event, GameList gameList, int currentGameOrder);
}
