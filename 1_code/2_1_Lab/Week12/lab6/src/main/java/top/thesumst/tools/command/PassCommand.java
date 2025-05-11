package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import top.thesumst.mode.ReversiMode;
import top.thesumst.tools.PrintTools;
import top.thesumst.exception.IllegalCommandException;

/**
 * 跳过回合命令
 */
public class PassCommand implements GameCommand {
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        try {
            if(!game.receiveOperation("pass"))
                throw new IllegalCommandException("跳过回合失败");
            return CommandResult.success("跳过回合成功");
        } catch (IllegalCommandException e) {
            return CommandResult.failure("跳过回合失败: " + e.getMessage());
        } finally {
            PrintTools.printBoard(game);
            PrintTools.printPlayerInfo(game);
        }
    }
}