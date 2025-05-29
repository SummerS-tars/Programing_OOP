package top.thesumst.view.gui;

import top.thesumst.core.container.GameList;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public class GUIView implements Observer
{
    @Override
    public void update(Event event, GameList gameList, int currentGameOrder) {}

    @Override
    public void init(GameList gameList, int currentGameOrder) {}
}
