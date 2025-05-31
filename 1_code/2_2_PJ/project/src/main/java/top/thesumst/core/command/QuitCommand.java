package top.thesumst.core.command;

import javafx.application.Platform;
import top.thesumst.core.container.GameContainer;
import top.thesumst.persistence.PersistenceManager;

/**
 * 退出游戏命令
 * 现在只处理保存检查，不直接强制退出
 */
public class QuitCommand implements GameCommand {    @Override
    public CommandResult execute() 
    {
        // 检查是否需要保存并处理退出逻辑
        boolean isGuiMode = Platform.isFxApplicationThread() || isJavaFXEnvironment();
        boolean canExit = PersistenceManager.checkAndSaveOnExit(GameContainer.getCurrentInstance(), isGuiMode);
        
        if (canExit) {
            // 只有用户确认可以退出时才真正退出
            GameContainer.stopGame();
            
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