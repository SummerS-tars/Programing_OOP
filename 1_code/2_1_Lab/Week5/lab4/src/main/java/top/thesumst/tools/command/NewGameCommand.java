package top.thesumst.tools.command;

import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;

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
        if (!gameType.equals("peace") && !gameType.equals("reversi")) 
        {
            return CommandResult.failure("不支持的游戏类型: " + gameType);
        }

        gameList.addGame(gameType);
        return CommandResult.success("已创建新的" + gameType + "游戏");
    }
    
    public String getGameType() 
    {
        return gameType;
    }
}