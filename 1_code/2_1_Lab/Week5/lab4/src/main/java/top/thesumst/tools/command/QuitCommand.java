package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;

/**
 * 退出游戏命令
 */
public class QuitCommand implements GameCommand {
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        game.receiveOperation("quit");
        return CommandResult.quit();
    }
}