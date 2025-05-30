package top.thesumst.core.command;

public class InvalidCommand implements GameCommand
{
    private String input;

    public InvalidCommand(String input) 
    {
        this.input = input;
    }

    @Override
    public CommandResult execute() 
    {
        return CommandResult.failure("无效命令: " + input);
    }
}