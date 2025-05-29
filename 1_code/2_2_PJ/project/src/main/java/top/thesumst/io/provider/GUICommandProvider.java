package top.thesumst.io.provider;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUICommandProvider extends BaseCommandProvider
{
    private final BlockingQueue<String> commandQueue;
    
    // 添加构造函数调用父类构造器
    public GUICommandProvider() {
        super(CommandProviderMode.GUI);
        this.commandQueue = new LinkedBlockingQueue<>();
    }

    // 为GUI提供设置命令的方法
    public void setInputBuffer(String command) {
        this.inputBuffer = command;
    }

    // 为GUI提供添加命令到队列的方法
    public void offerCommand(String command) {
        commandQueue.offer(command);
    }

    @Override
    public void getNextCommand()
    {
        if (inputBuffer == null || inputBuffer.trim().isEmpty()) {
            try {
                // 从命令队列中获取下一个命令
                inputBuffer = commandQueue.take(); // 阻塞等待命令
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inputBuffer = null;
            }
        }
    }

    @Override
    public boolean hasCommand()
    {
        return (inputBuffer != null && !inputBuffer.trim().isEmpty()) || !commandQueue.isEmpty();
    }

    @Override
    public void open() {
        // GUI模式下，无需特殊的打开操作
    }

    @Override
    public void close() {
        // 清空命令队列
        commandQueue.clear();
    }
}