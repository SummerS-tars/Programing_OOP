package top.thesumst.core.command;

import javafx.application.Platform;
import top.thesumst.core.container.GameContainer;

/**
 * 退出游戏命令
 */
public class QuitCommand implements GameCommand {
    @Override
    public CommandResult execute() 
    {
        GameContainer.stopGame();
        
        // 如果是JavaFX环境，确保完全退出应用程序
        if (Platform.isFxApplicationThread() || isJavaFXEnvironment()) {
            Platform.exit();
        }
        
        return CommandResult.quit();
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