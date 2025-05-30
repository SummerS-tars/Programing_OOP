package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.mode.GameMode;
import top.thesumst.type.*;
import top.thesumst.type.exception.IllegalMoveException;

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
    public CommandResult execute() 
    {
        // 执行下棋操作
        try {
            GameMode game = GameContainer.getCurrentGame();
            if(!game.receiveOperation(new Operation<Point>(OperationType.MOVE, position)))
                throw new IllegalMoveException();
            String result = "("+(position.x+1)+","+(char)('A'+position.y)+")"+"着棋成功";
            return CommandResult.success(result);
        } catch (IllegalMoveException e) {
            return CommandResult.failure("下棋失败: " + e.getMessage());
        }
    }
}
