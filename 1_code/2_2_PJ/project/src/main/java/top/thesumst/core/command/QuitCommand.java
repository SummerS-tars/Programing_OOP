package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;

/**
 * 退出游戏命令
 */
public class QuitCommand implements GameCommand {
    @Override
    public CommandResult execute() 
    {
        GameContainer.stopGame();
        return CommandResult.quit();
    }
}