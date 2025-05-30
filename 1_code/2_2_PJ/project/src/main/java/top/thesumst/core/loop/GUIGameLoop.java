package top.thesumst.core.loop;

import javafx.application.Platform;
import top.thesumst.core.command.PlaybackCommand;
import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public class GUIGameLoop extends GameLoop
{
    private Thread gameLoopThread;
    private volatile boolean shouldStop = false;
    
    public GUIGameLoop(GameList gameList, BaseCommandProvider cmdProvider, Observer observer)
    {
        super(gameList, cmdProvider, observer);
    }

    @Override
    public void startLoop()
    {
        isRunning = true;
        shouldStop = false;
        cmdProvider.open();
        
        // 在单独的线程中运行游戏循环，避免阻塞JavaFX应用线程
        gameLoopThread = new Thread(() -> {
            try {
                gameLoop();
            } catch (Exception e) {
                System.err.println("GUIGameLoop - 游戏循环异常: " + e.getMessage());
                e.printStackTrace();
            }
        }, "GUIGameLoop-Thread");
        
        gameLoopThread.setDaemon(true); // 设置为守护线程
        gameLoopThread.start();
        
        System.out.println("GUIGameLoop - 游戏循环已启动");
    }

    @Override
    public void stopLoop()
    {
        System.out.println("GUIGameLoop - 停止游戏循环");
        isRunning = false;
        shouldStop = true;
        
        if (gameLoopThread != null && gameLoopThread.isAlive()) {
            gameLoopThread.interrupt();
            try {
                gameLoopThread.join(1000); // 等待最多1秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        cmdProvider.close();
        System.out.println("GUIGameLoop - 游戏循环已停止");
    }

    @Override    
    public void gameLoop()
    {
        Event event = null;
        while(isRunning && !shouldStop)
        {
            try {
                event = null;
                
                // 获取下一个命令
                cmdProvider.getNextCommand();
                
                // 如果线程被中断或需要停止，退出循环
                if (Thread.currentThread().isInterrupted() || shouldStop) {
                    break;
                }
                
                // 解析命令并创建事件
                event = cmdProvider.getEvent();
                
                if (event != null) {
                    // 执行事件
                    event.executeEvent(gameList, currentGameOrder);
                    
                    // 在JavaFX应用线程中通知观察者（更新UI）
                    final Event finalEvent = event;
                    Platform.runLater(() -> {
                        try {
                            notifyObservers(finalEvent, gameList, currentGameOrder);
                        } catch (Exception e) {
                            System.err.println("GUIGameLoop - 通知观察者时发生异常: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                    
                    // 处理回放命令
                    if(event.getCommand() instanceof PlaybackCommand) {
                        playback(event);
                    }
                }
                
                // 让出CPU时间片，避免占用过多资源
                Thread.sleep(10);
                
            } catch (InterruptedException e) {
                // 线程被中断，停止循环
                Thread.currentThread().interrupt();
                System.out.println("GUIGameLoop - 游戏循环线程被中断");
                break;
            } catch (Exception e) {
                System.err.println("GUIGameLoop - 游戏循环中发生异常: " + e.getMessage());
                e.printStackTrace();
                
                // 在JavaFX应用线程中显示错误信息
                Platform.runLater(() -> {
                    try {
                        // 创建一个错误事件来传递错误信息
                        Event errorEvent = new Event();
                        errorEvent.setMessage("游戏循环异常: " + e.getMessage());
                        notifyObservers(errorEvent, gameList, currentGameOrder);
                    } catch (Exception notifyException) {
                        System.err.println("GUIGameLoop - 通知错误时发生异常: " + notifyException.getMessage());
                    }
                });
            }
        }
        
        System.out.println("GUIGameLoop - 游戏循环已结束");
    }
}