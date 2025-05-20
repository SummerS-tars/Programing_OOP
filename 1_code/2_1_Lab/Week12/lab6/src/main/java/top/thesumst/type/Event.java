package top.thesumst.type;

import top.thesumst.core.command.GameCommand;

/**
 * Event类用于表示游戏中的事件
 * 包含：1. 事件类型；2. 原始命令；3. 命令对象；
 * 4. 事件数据；5. 处理成功与否；6. 消息
 */
public class Event
{
    private EventType type; // 主要表示应该在哪里执行，没有太细粒度
    private String rawCommand;
    private GameCommand command; // 用来执行，然后用来获取部分信息，如果为null说明是无效输入
    private Object data; // 不一定有用
    private EventState state; // 事件状态
    private String message;
    
    public String getRawCommand() {
        return rawCommand;
    }
    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }
    public EventType getType() {
        return type;
    }
    public void setType(EventType type) {
        this.type = type;
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
                "type=" + type +
                ", rawCommand='" + rawCommand + '\'' +
                ", command=" + command +
                ", data=" + data +
                ", state=" + state +
                ", message='" + message + '\'' +
                '}';
    }
}
