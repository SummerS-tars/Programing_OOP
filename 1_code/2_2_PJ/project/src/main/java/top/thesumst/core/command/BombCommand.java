package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.mode.*;
import top.thesumst.type.*;
import top.thesumst.type.exception.*;

import java.awt.Point;

public class BombCommand implements GameCommand
{
    private final Point position;

    public BombCommand(Point position) 
    {
        this.position = position;
    }

    @Override
    public CommandResult execute() {
        // 执行炸弹操作
        try {
            GameMode game = GameContainer.getCurrentGame();
            if (!game.receiveOperation(new Operation<Point>(OperationType.BOMB, position)))
                throw new IllegalMoveException();
            String result = "(" + (position.x + 1) + "," + (char) ('A' + position.y) + ")" + "使用炸弹成功";
            return CommandResult.success(result);
        } catch (IllegalCommandException e) {
            return CommandResult.failure("炸弹失败: " + e.getMessage());
        }
    }
}
