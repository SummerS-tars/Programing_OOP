package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;

/**
 * 退出游戏命令
 */
public class QuitCommand implements GameCommand {
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        GameContainer.stopGame();
        return CommandResult.quit();
    }
}