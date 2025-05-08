package top.thesumst.tools.command;

/**
 * 命令执行结果
 */
public class CommandResult 
{
    private final boolean success;
    private final String message;
    private final boolean shouldRefreshDisplay;
    private final boolean shouldQuit;
    
    // 构造函数
    private CommandResult(boolean success, String message, boolean shouldRefreshDisplay, boolean shouldQuit) 
    {
        this.success = success;
        this.message = message;
        this.shouldRefreshDisplay = shouldRefreshDisplay;
        this.shouldQuit = shouldQuit;
    }

    // 工厂方法
    public static CommandResult success() 
    {
        return new CommandResult(true, "", true, false);
    }
    
    public static CommandResult success(String message) 
    {
        return new CommandResult(true, message, true, false);
    }
    
    public static CommandResult failure(String message) 
    {
        return new CommandResult(false, message, false, false);
    }
    
    public static CommandResult quit() 
    {
        return new CommandResult(true, "退出游戏", false, true);
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
    
    public boolean shouldRefreshDisplay() 
    {
        return shouldRefreshDisplay;
    }
    
    public boolean shouldQuit() 
    {
        return shouldQuit;
    }
}
