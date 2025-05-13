package top.thesumst.command;

import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.exception.IllegalMoveException;
import top.thesumst.tools.PrintTools;
import top.thesumst.type.*;

import java.awt.Point;

/**
 * 下棋命令
 */
public class GoCommand implements GameCommand 
{
    private final Point position;
    
    public GoCommand(Point position) 
    {
        this.position = position;
    }

    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        // 执行下棋操作
        try {
            if(!game.receiveOperation(new Operation<Point>(OperationType.MOVE, position)))
                throw new IllegalMoveException();
            String result = "("+(position.x+1)+","+(char)('A'+position.y)+")"+"着棋成功";
            return CommandResult.success(result);
        } catch (IllegalMoveException e) {
            return CommandResult.failure("下棋失败: " + e.getMessage());
        } finally {
            PrintTools.printBoard(game);
            PrintTools.printPlayerInfo(game);
        }
    }
}
