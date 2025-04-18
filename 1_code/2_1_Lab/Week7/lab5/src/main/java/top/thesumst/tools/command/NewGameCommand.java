package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import top.thesumst.tools.PrintTools;

/**
 * 创建新游戏命令
 */
public class NewGameCommand implements GameCommand {
    private final String gameType;
    
    public NewGameCommand(String gameType) 
    {
        this.gameType = gameType;
    }
    
    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        if (!gameType.equals("peace") && 
            !gameType.equals("reversi") &&
            !gameType.equals("gomoku")) 
        {
            return CommandResult.failure("不支持的游戏类型: " + gameType);
        }

        if(gameList.addGame(gameType))
        {
            PrintTools.printGameList(gameList);
            return CommandResult.success("已创建新的" + gameType + "游戏");
        }
        else
        {
            return CommandResult.failure("因游戏数量已达上限，无法创建新游戏");
        }
    }
    
    public String getGameType() 
    {
        return gameType;
    }
}