package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.type.*;
import top.thesumst.type.exception.IllegalCommandException;

/**
 * 跳过回合命令
 */
public class PassCommand implements GameCommand {
    @Override
    public CommandResult execute() 
    {
        try {
            GameMode game = GameContainer.getCurrentGame();
            if(!game.receiveOperation(new Operation<Void>(OperationType.PASS, null)))
                throw new IllegalCommandException("跳过回合失败");
            return CommandResult.success("跳过回合成功");
        } catch (IllegalCommandException e) {
            return CommandResult.failure("跳过回合失败: " + e.getMessage());
        }
    }
}