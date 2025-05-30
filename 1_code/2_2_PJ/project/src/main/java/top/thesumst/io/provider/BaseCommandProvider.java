package top.thesumst.io.provider;

import java.awt.Point;

import top.thesumst.core.command.CommandFactory;
import top.thesumst.io.input.*;
import top.thesumst.type.*;


public abstract class BaseCommandProvider
{
    public enum CommandProviderMode 
    {
        CLI, // 命令行模式
        GUI, // 图形界面模式
        PLAYBACK // 回放模式
    }

    protected final CommandProviderMode mode;
    protected String inputBuffer;

    protected BaseCommandProvider(CommandProviderMode mode)
    {
        this.mode = mode;
    }

    abstract public void getNextCommand(); // 获取下一个命令

    abstract public boolean hasCommand(); // 是否有命令

    abstract public void open(); // 打开命令提供者

    abstract public void close(); // 关闭命令提供者

    public CommandProviderMode getMode() // 获取命令提供者模式
    {
        return mode;
    }

    /**
     * 将输入的原始命令解析为事件
     * @param rawCommand
     * @return
     */
    public Event getEvent()
    {
        String rawCommand = inputBuffer;
        Event event = new Event();
        event.setRawCommand(rawCommand);
        event.setState(EventState.EVENT_GET);

        InputResult inputResult = InputParser.parse(rawCommand);
        event.setCommand(CommandFactory.createCommand(inputResult));
        switch (inputResult.getType()) 
        {
            case CHESS_MOVE:
            case USE_BOMB:
                event.setData(inputResult.<Point>getDataAs(Point.class));
                break;
            case SWITCH_BOARD:
                event.setData(inputResult.<Integer>getDataAs(Integer.class));
                break;
            case NEW_GAME:
                event.setData(inputResult.<String>getDataAs(String.class));
                break;
            case PLAYBACK:
                event.setData(inputResult.<String>getDataAs(String.class));
                break;
            default:
                event.setData(null);
                break;
        }
        event.setState(EventState.EVENT_PARSED);

        inputBuffer = null; // 清空输入缓冲区
        return event;
    }
}
