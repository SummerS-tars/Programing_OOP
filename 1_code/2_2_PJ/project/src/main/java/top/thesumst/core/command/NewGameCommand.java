package top.thesumst.core.command;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.type.exception.IllegalCommandException;
import top.thesumst.view.console.CLIPrintTools;

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
    public CommandResult execute() 
    {
        try {
            GameList gameList = GameContainer.getGameList();
            if(GameList.addGame(gameType))
            {
                CLIPrintTools.printGameList(gameList);
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