package top.thesumst.io.provider;

public class GUICommandProvider extends BaseCommandProvider
{
    // 添加构造函数调用父类构造器
    public GUICommandProvider() {
        super(CommandProviderMode.GUI);
    }

    // TODO: 具体GUI功能待实现
    @Override
    public void getNextCommand()
    {
    }

    @Override
    public boolean hasCommand()
    {
        return false;
    }

    @Override
    public void open() {}

    @Override
    public void close() {}
}