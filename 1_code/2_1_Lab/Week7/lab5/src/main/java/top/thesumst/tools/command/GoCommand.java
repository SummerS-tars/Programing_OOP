package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import top.thesumst.mode.ReversiMode;
import top.thesumst.tools.PrintTools;

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
        if(game.isOver())
        {
            return CommandResult.failure("游戏已经结束，无法下棋");
        }

        if(game instanceof ReversiMode && ((ReversiMode)game).shouldPass())
        {
            return CommandResult.failure("当前无有效下棋位置，无法下棋，请执行pass");
        }

        // 执行下棋操作
        boolean success = game.receiveOperation(position);
        PrintTools.printBoard(game);
        PrintTools.printPlayerInfo(game);

        String result = "("+(position.x+1)+","+(char)('A'+position.y)+")"+"着棋成功";
        if (success) return CommandResult.success(result);
        else return CommandResult.failure("无效的下棋位置");
    }
}
