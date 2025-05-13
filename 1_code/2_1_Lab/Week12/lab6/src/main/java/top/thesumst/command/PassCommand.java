package top.thesumst.command;

import top.thesumst.tools.PrintTools;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.exception.IllegalCommandException;
import top.thesumst.type.*;

/**
 * 跳过回合命令
 */
public class PassCommand implements GameCommand {
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        try {
            if(!game.receiveOperation(new Operation<Void>(OperationType.PASS, null)))
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