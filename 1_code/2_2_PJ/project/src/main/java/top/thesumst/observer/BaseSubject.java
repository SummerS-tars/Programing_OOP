package top.thesumst.observer;

import java.util.ArrayList;
import java.util.List;

import top.thesumst.core.container.GameList;
import top.thesumst.type.Event;

public abstract class BaseSubject implements Subject
{
    List<Observer> observers = new ArrayList<>();
    boolean changed = false;

    @Override
    public void registerObserver(Observer observer)
    {
        if (observer != null && !observers.contains(observer))
        {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event, GameList gameList, int currentGameOrder)
    {
        for (Observer observer : observers)
        {
            observer.update(event, gameList, currentGameOrder);
        }
    }

    public void notifyInit(GameList gameList, int currentGameOrder)
    {
        for (Observer observer : observers)
        {
            observer.init(gameList, currentGameOrder);
        }
    }
}
