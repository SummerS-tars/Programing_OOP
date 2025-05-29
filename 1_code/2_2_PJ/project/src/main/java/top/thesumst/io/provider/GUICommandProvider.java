package top.thesumst.io.provider;

public class GUICommandProvider extends BaseCommandProvider
{
    // 添加构造函数调用父类构造器
    public GUICommandProvider() {
        super(CommandProviderMode.GUI);
    }

    // 为GUI提供设置命令的方法
    public void setInputBuffer(String command) {
        this.inputBuffer = command;
    }

    // TODO: 具体GUI功能待实现
    @Override
    public void getNextCommand()
    {
    }

    @Override
    public boolean hasCommand()
    {
        return inputBuffer != null && !inputBuffer.trim().isEmpty();
    }

    @Override
    public void open() {}

    @Override
    public void close() {}
}