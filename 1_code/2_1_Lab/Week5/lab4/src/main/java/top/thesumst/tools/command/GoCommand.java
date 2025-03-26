package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import java.awt.Point;

/**
 * 下棋命令
 */
public class GoCommand implements GameCommand {
    private final Point position;
    
    public GoCommand(Point position) 
    {
        this.position = position;
    }

    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {        
        // 执行下棋操作
        boolean success = game.receiveOperation(position);
        
        if (success) return CommandResult.success();
        else return CommandResult.failure("无效的下棋位置");
    }
}
