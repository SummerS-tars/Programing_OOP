package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import top.thesumst.mode.ReversiMode;
import top.thesumst.tools.PrintTools;

/**
 * 跳过回合命令
 */
public class PassCommand implements GameCommand {
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        if(game instanceof ReversiMode)
        {
            boolean success = game.receiveOperation("pass");
            PrintTools.printBoard(game);
    
            // 执行跳过操作
            if(success) return CommandResult.success("pass成功");
            else return CommandResult.failure("pass失败");
        }
        else return CommandResult.failure("当前模式不支持pass操作");
    }
}