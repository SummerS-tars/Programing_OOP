package top.thesumst.core.command;

import java.io.InputStream;

import top.thesumst.io.provider.PlaybackCommandProvider;
import top.thesumst.type.exception.IllegalCommandException;

/**
 * 回放命令，主要是用来检测是否存在此回放文件
 */
public class PlaybackCommand implements GameCommand
{
    String filename;

    public PlaybackCommand(String filename)
    {
        this.filename = filename;
    }

    @Override
    public CommandResult execute() 
    {
        try {
            // 检测文件是否存在
            if (filename == null || filename.isEmpty())
                throw new IllegalCommandException("回放文件名不能为空");

            String resourcePath = "/top/thesumst/scripts/" + filename;
            InputStream inputStream = PlaybackCommandProvider.class.getResourceAsStream(resourcePath);
            if (inputStream == null)
                throw new IllegalCommandException("无法在类路径中找到资源: " + resourcePath);
            return CommandResult.success("Demo模式开始使用: " + filename);
        } catch (IllegalCommandException e) {
            return CommandResult.failure("回放失败: " + e.getMessage());
        }
    }
}
