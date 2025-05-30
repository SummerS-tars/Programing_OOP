package top.thesumst.core.command;

import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;

public class InvalidCommand implements GameCommand
{
    private String input;

    public InvalidCommand(String input) 
    {
        this.input = input;
    }

    @Override
    public CommandResult execute(GameMode game, GameList gameList) 
    {
        return CommandResult.failure("无效命令: " + input);
    }
}