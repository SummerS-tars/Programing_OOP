package top.thesumst.view.console;

import top.thesumst.core.container.GameList;
import top.thesumst.observer.*;
import top.thesumst.type.Event;

public class CLIView implements Observer
{
    @Override
    public void update(Event event, GameList gameList, int currentGameOrder)
    {
        CLIPrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
        CLIPrintTools.clearConsole();
        CLIPrintTools.printBoard(GameList.getGame(currentGameOrder));
        CLIPrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
        CLIPrintTools.printGameList(gameList);
        CLIPrintTools.printTipPanel(gameList);
        CLIPrintTools.printInputPanel(gameList, event);
    }

    @Override
    public void init(GameList gameList, int currentGameOrder)
    {
        CLIPrintTools.initializePositionsSet(GameList.getGame(currentGameOrder));
        CLIPrintTools.clearConsole();
        CLIPrintTools.printBoard(GameList.getGame(currentGameOrder));
        CLIPrintTools.printPlayerInfo(GameList.getGame(currentGameOrder));
        CLIPrintTools.printGameList(gameList);
        CLIPrintTools.printTipPanel(gameList);
        CLIPrintTools.printInputPanel(gameList, null);
    }
}
