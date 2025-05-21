package top.thesumst.view.console;

import top.thesumst.core.container.GameList;
import top.thesumst.observer.*;
import top.thesumst.tools.PrintTools;
import top.thesumst.type.Event;

public class CLIView implements Observer
{
    @Override
    public void update(Event event, GameList gameList, int currentGameOrder)
    {
        PrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
        PrintTools.clearConsole();
        PrintTools.printBoard(GameList.getGame(currentGameOrder));
        PrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
        PrintTools.printGameList(gameList);
        PrintTools.printTipPanel(gameList);
        PrintTools.printInputPanel(gameList, event);
    }

    @Override
    public void init(GameList gameList, int currentGameOrder)
    {
        PrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
        PrintTools.clearConsole();
        PrintTools.printBoard(GameList.getGame(currentGameOrder));
        PrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
        PrintTools.printGameList(gameList);
        PrintTools.printTipPanel(gameList);
        PrintTools.printInputPanel(gameList, null);
    }
}
