package top.thesumst.view.gui;

import javafx.application.Platform;
import top.thesumst.core.container.GameList;
import top.thesumst.observer.Observer;
import top.thesumst.type.Event;

public class GUIView implements Observer
{
    private GUIController controller;
    
    /**
     * 默认构造函数（用于ViewFactory）
     */
    public GUIView() {
        this.controller = null;
    }
    
    /**
     * 带Controller的构造函数
     */
    public GUIView(GUIController controller) {
        this.controller = controller;
    }
    
    /**
     * 设置Controller（用于后期绑定）
     */
    public void setController(GUIController controller) {
        this.controller = controller;
    }
    
    @Override
    public void update(Event event, GameList gameList, int currentGameOrder) {
        // 检查JavaFX Toolkit是否已初始化
        try {
            if (javafx.application.Platform.isFxApplicationThread() || isJavaFXToolkitInitialized()) {
                // 确保UI更新在JavaFX应用线程中进行
                Platform.runLater(() -> {
                    if (controller != null) {
                        controller.updateGameUI(event, gameList, currentGameOrder);
                        
                        // 如果有错误消息，显示给用户
                        if (event.getMessage() != null && !event.getMessage().isEmpty()) {
                            controller.displayMessage(event.getMessage());
                        }
                    }
                });
            } else {
                // JavaFX Toolkit未初始化，直接调用（这种情况下controller通常为null）
                if (controller != null) {
                    controller.updateGameUI(event, gameList, currentGameOrder);
                    if (event.getMessage() != null && !event.getMessage().isEmpty()) {
                        controller.displayMessage(event.getMessage());
                    }
                }
            }
        } catch (IllegalStateException e) {
            // JavaFX Toolkit未初始化，忽略UI更新
            System.out.println("GUIView.update(): JavaFX Toolkit未初始化，跳过UI更新");
        }
    }

    @Override
    public void init(GameList gameList, int currentGameOrder) {
        // 检查JavaFX Toolkit是否已初始化
        try {
            // 尝试检查Platform是否可用
            if (javafx.application.Platform.isFxApplicationThread() || isJavaFXToolkitInitialized()) {
                // 如果在JavaFX线程中或Toolkit已初始化，使用Platform.runLater
                Platform.runLater(() -> {
                    if (controller != null) {
                        controller.initializeGameUI(gameList, currentGameOrder);
                    }
                });
            } else {
                // 如果Toolkit未初始化，直接调用（这种情况下controller通常为null）
                if (controller != null) {
                    controller.initializeGameUI(gameList, currentGameOrder);
                }
            }
        } catch (IllegalStateException e) {
            // JavaFX Toolkit未初始化，忽略UI初始化
            System.out.println("GUIView.init(): JavaFX Toolkit未初始化，跳过UI初始化");
        }
    }
    
    /**
     * 检查JavaFX Toolkit是否已初始化
     */
    private boolean isJavaFXToolkitInitialized() {
        try {
            // 尝试访问JavaFX Platform来检查是否已初始化
            Platform.runLater(() -> {});
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
