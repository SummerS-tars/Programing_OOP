package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.exception.*;
import top.thesumst.mode.*;
import top.thesumst.type.*;
import top.thesumst.tools.PrintTools;
import java.awt.Point;

public class BombCommand implements GameCommand
{
    private final Point position;

    public BombCommand(Point position) 
    {
        this.position = position;
    }

    @Override
    public CommandResult execute(GameMode game, GameList gameList) {
        // 执行炸弹操作
        try {
            if (!game.receiveOperation(new Operation<Point>(OperationType.BOMB, position)))
                throw new IllegalMoveException();
            String result = "(" + (position.x + 1) + "," + (char) ('A' + position.y) + ")" + "使用炸弹成功";
            return CommandResult.success(result);
        } catch (IllegalCommandException e) {
            return CommandResult.failure("炸弹失败: " + e.getMessage());
        } finally {
            PrintTools.printBoard(game);
            PrintTools.printPlayerInfo(game);
        }
    }
}
