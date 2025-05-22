package top.thesumst.io.provider;

import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;

public class CommandProviderFactory 
{
    public static BaseCommandProvider getCommandProvider(String providerType)
    {
        if(providerType.equals("cli")) return new CLICommandProvider(CommandProviderMode.CLI);
        else if(providerType.equals("gui")) return new GUICommandProvider();
        else throw new IllegalArgumentException("命令提供器错误: " + providerType);
    }
}
