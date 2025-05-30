package top.thesumst.type;

import top.thesumst.core.command.CommandResult;
import top.thesumst.core.command.GameCommand;
import top.thesumst.core.container.GameList;

/**
 * Event类用于表示游戏中的事件
 * 包含：1. 事件类型；2. 原始命令；3. 命令对象；
 * 4. 事件数据；5. 处理成功与否；6. 消息
 */
public class Event
{
    private String rawCommand;
    private GameCommand command; // 用来执行，然后用来获取部分信息，如果为null说明是无效输入
    private Object data; // 不一定有用
    private EventState state; // 事件状态
    private String message;

    public void executeEvent(GameList gameList, int currentGameOrder)
    {
        CommandResult result = getCommand().execute(GameList.getGame(currentGameOrder), gameList);
        if(result.isSuccess()) setState(EventState.EVENT_EXECUTED_SUCCESS);
        else setState(EventState.EVENT_EXECUTED_FAIL);
        setMessage(result.getMessage());
    }

    public String getRawCommand() {
        return rawCommand;
    }
    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }
    public GameCommand getCommand() {
        return command;
    }
    public void setCommand(GameCommand command) {
        this.command = command;
    }
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public EventState getState() {
        return state;
    }
    public void setState(EventState state) {
        this.state = state;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "Event{" +
                ", rawCommand='" + rawCommand + '\'' +
                ", command=" + (command == null ? "null" : command.getClass().getSimpleName()) +
                ", data=" + data +
                ", state=" + state +
                ", message='" + message + '\'' +
                '}';
    }

    public static Event getErrorEvent(Exception e)
    {
        Event event = new Event();
        event.setMessage("发生错误: " + e.getMessage());
        return event;
    }
}
