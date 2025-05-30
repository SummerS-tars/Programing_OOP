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

    // 为GUI提供添加命令到队列的方法
    public void offerCommand(String command) {
        if (command != null && !command.trim().isEmpty()) {
            commandQueue.offer(command.trim());
            System.out.println("GUICommandProvider - 命令已添加到队列: " + command.trim());
        }
    }

    // 为测试提供的方法，用于获取当前输入缓冲区内容
    public String getCurrentInputBuffer() {
        return inputBuffer;
    }

    // 为测试提供的方法，用于检查队列是否为空
    public boolean isQueueEmpty() {
        return commandQueue.isEmpty();
    }

    // 为测试提供的方法，用于获取队列大小
    public int getQueueSize() {
        return commandQueue.size();
    }

    // 为测试提供的方法，用于设置输入缓冲区内容
    public void setInputBuffer(String buffer) {
        this.inputBuffer = buffer;
        System.out.println("GUICommandProvider - 设置输入缓冲区: " + buffer);
    }

    @Override
    public void getNextCommand()
    {
        if (inputBuffer == null || inputBuffer.trim().isEmpty()) {
            try {
                // 从命令队列中获取下一个命令，阻塞等待
                inputBuffer = commandQueue.take();
                System.out.println("GUICommandProvider - 从队列获取命令: " + inputBuffer);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inputBuffer = null;
                System.out.println("GUICommandProvider - 命令获取被中断");
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
        System.out.println("GUICommandProvider - 已打开");
    }

    @Override
    public void close() {
        // 清空命令队列
        commandQueue.clear();
        inputBuffer = null;
        System.out.println("GUICommandProvider - 已关闭，队列已清空");
    }
}