package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;

public class SwitchBoardCommand implements GameCommand
{
    private final int boardNumber;
    
    public SwitchBoardCommand(int boardNumber) 
    {
        this.boardNumber = boardNumber;
    }
    
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        // 检查棋盘编号是否合法
        if (boardNumber < 1 || boardNumber > GameList.getGameNumber()) 
        {
            return CommandResult.failure("无效的棋盘编号");
        }

        // 切换到指定棋盘
        GameContainer.switchGameOrder(boardNumber);
        game = GameList.getGame(boardNumber);

        return CommandResult.success("切换到棋盘 #" + boardNumber);
    }

    public int getBoardNumber() {
        return boardNumber;
    }
}
