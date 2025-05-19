package top.thesumst.type;

/**
 * Event类用于表示游戏中的事件
 * 包含事件类型、事件数据、处理成功与否和消息等信息
 */
public class Event 
{
    private EventType type;
    private Object data;
    private Boolean handleSuccess;
    private String message;
    public EventType getType() {
        return type;
    }
    public void setType(EventType type) {
        this.type = type;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public Boolean getHandleSuccess() {
        return handleSuccess;
    }
    public void setHandleSuccess(Boolean handleSuccess) {
        this.handleSuccess = handleSuccess;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
