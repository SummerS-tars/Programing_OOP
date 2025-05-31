package top.thesumst;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.loop.GameLoop;
import top.thesumst.core.loop.GameLoopFactory;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.io.provider.CommandProviderFactory;
import top.thesumst.observer.Observer;
import top.thesumst.view.ViewFactory;
import top.thesumst.view.console.CLIPrintTools;
import top.thesumst.persistence.PersistenceManager;
import top.thesumst.persistence.GameSaveData;

/**
 * 应用程序启动器类
 * 该类不直接继承 javafx.application.Application，
 * 用于解决打包后的JAR文件出现"缺少JavaFX运行时组件"的问题
 */
public class Launcher {
    /**
     * 程序入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 检查启动参数，如果是GUI模式则启动JavaFX应用
        if (args.length > 0 && "gui".equals(args[0])) {
            // 调用原始的 JavaFX Application 主类的 launch 方法，启动GUI模式
            Main.launch(Main.class, args);
        } else {
            // 传统CLI模式
            CLIPrintTools.gameMotd();
            
            // 检查是否有存档要加载
            GameSaveData saveData = PersistenceManager.checkAndLoadOnStartup(false);
            
            try {
                Observer observer = ViewFactory.getView(args.length > 0 ? args[0] : "cli");
                BaseCommandProvider cmdProvider = CommandProviderFactory.getCommandProvider(args.length > 0 ? args[0] : "cli");
                GameList gameList;
                GameLoop gameLoop;
                GameContainer gameContainer;
                
                // 创建或恢复游戏状态
                if (saveData != null) {
                    // 从存档恢复游戏状态
                    gameList = saveData.gameList;
                    
                    gameLoop = GameLoopFactory.getGameLoop(args.length > 0 ? args[0] : "cli", gameList, cmdProvider, observer);
                    gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
                    
                    // 恢复游戏状态到容器中
                    PersistenceManager.restoreGameState(gameContainer, saveData);
                    
                    System.out.println("成功从存档恢复游戏状态");
                } else {
                    // 创建新的游戏列表
                    gameList = new GameList();
                    
                    gameLoop = GameLoopFactory.getGameLoop(args.length > 0 ? args[0] : "cli", gameList, cmdProvider, observer);
                    gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
                }
                // 添加JVM关闭钩子来处理保存
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    // 只在非正常退出时显示保存提示
                    // QuitCommand会处理正常退出的保存逻辑
                    if (gameContainer != null && !GameContainer.isNormalShutdown()) {
                        System.out.println("\n程序即将退出...");
                        PersistenceManager.checkAndSaveOnExit(gameContainer, false);
                    }
                }));
                
                gameContainer.runGame();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("启动模式参数错误: " + e.getMessage());
                System.out.println("请使用 'cli' 或 'gui' 启动游戏");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("发生了未知错误: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
