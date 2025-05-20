package top.thesumst.core.command;

/**
 * 命令执行结果
 */
public class CommandResult 
{
    private final boolean success;
    private final String message;
    private final boolean shouldQuit;
    
    // 构造函数
    private CommandResult(boolean success, String message, boolean shouldQuit) 
    {
        this.success = success;
        this.message = message;
        this.shouldQuit = shouldQuit;
    }

    // 工厂方法
    public static CommandResult success() 
    {
        return new CommandResult(true, "", false);
    }
    
    public static CommandResult success(String message) 
    {
        return new CommandResult(true, message, false);
    }
    
    public static CommandResult failure(String message) 
    {
        return new CommandResult(false, message, false);
    }
    
    public static CommandResult quit() 
    {
        return new CommandResult(true, "退出游戏", true);
    }

    // Getter方法
    public boolean isSuccess() 
    {
        return success;
    }
    
    public String getMessage() 
    {
        return message;
    }
    
    public boolean shouldQuit() 
    {
        return shouldQuit;
    }
}
