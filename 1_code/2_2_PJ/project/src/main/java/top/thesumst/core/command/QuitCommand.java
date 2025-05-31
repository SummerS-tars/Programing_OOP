package top.thesumst.core.command;

import javafx.application.Platform;
import top.thesumst.core.container.GameContainer;
import top.thesumst.persistence.PersistenceManager;

/**
 * 退出游戏命令
 * 现在只处理保存检查，不直接强制退出
 */
public class QuitCommand implements GameCommand {    @Override    public CommandResult execute() 
    {
        // 检查命令是从哪个模式（GUI或CLI）发出的
        // 尊重命令提供者的类型，而不是检测JavaFX环境
        // 这避免了在CLI模式下弹出GUI对话框
        boolean isGuiMode = false;
        
        // 获取当前游戏容器
        GameContainer gameContainer = GameContainer.getCurrentInstance();
        
        // 如果命令提供者是GUI类型的，则使用GUI模式
        if (gameContainer != null && gameContainer.getCommandProvider() instanceof top.thesumst.io.provider.GUICommandProvider) {
            isGuiMode = true;
        }
        
        System.out.println("QuitCommand: 使用" + (isGuiMode ? "GUI" : "CLI") + "模式处理退出");
        
        // 检查是否需要保存并处理退出逻辑
        boolean canExit = PersistenceManager.checkAndSaveOnExit(gameContainer, isGuiMode);
        
        if (canExit) {
            // 只有用户确认可以退出时才真正退出
            // 停止游戏前标记这是正常退出
            GameContainer.stopGame(); // 这会设置normalShutdown = true
            
            if (isGuiMode) {
                Platform.exit();
            }
            System.exit(0);
        }
        
        // 如果用户取消退出，返回继续游戏的结果
        return CommandResult.success();
    }
    
    /**
     * 检查是否在JavaFX环境中运行
     */
    private boolean isJavaFXEnvironment() {
        try {
            // 尝试获取JavaFX工具包，如果存在说明在JavaFX环境中
            Platform.runLater(() -> {});
            return true;
        } catch (IllegalStateException e) {
            // JavaFX工具包未初始化，说明不在JavaFX环境中
            return false;
        }
    }
}