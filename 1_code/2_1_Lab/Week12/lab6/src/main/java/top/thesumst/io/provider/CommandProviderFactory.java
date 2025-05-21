package top.thesumst.io.provider;

public class CommandProviderFactory 
{
    public static BaseCommandProvider getCommandProvider(String providerType)
    {
        if(providerType.equals("cli")) return new CLICommandProvider();
        else if(providerType.equals("gui")) return new GUICommandProvider();
        else throw new IllegalArgumentException("命令提供器错误: " + providerType);
    }
}
