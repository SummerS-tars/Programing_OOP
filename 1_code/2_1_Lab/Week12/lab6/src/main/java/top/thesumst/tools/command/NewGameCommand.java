package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.exception.IllegalCommandException;
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
        try {
            if(gameList.addGame(gameType))
            {
                PrintTools.printGameList(gameList);
                return CommandResult.success("已创建新的" + gameType + "游戏");
            }
            else throw new IllegalCommandException("创建新游戏失败");
        } catch (IllegalCommandException e) {
            return CommandResult.failure("创建新游戏失败: " + e.getMessage());
        }
    }
    
    public String getGameType() 
    {
        return gameType;
    }
}