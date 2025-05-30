package top.thesumst.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import top.thesumst.core.container.GameList;
import top.thesumst.core.loop.GUIGameLoop;
import top.thesumst.io.provider.GUICommandProvider;

import java.io.IOException;

/**
 * GUI应用程序主入口点
 * 测试第5阶段的GUI命令集成
 */
public class GUIApplication extends Application {
    
    private GUIController controller;
    private GUICommandProvider commandProvider;
    private GUIGameLoop gameLoop;
    private GUIView guiView;
    
    @Override
    public void start(Stage stage) throws IOException {
        // 加载FXML文件
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("/MainGameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        
        // 获取控制器
        controller = fxmlLoader.getController();
        
        // 初始化命令提供者
        commandProvider = new GUICommandProvider();
        controller.setCommandProvider(commandProvider);
        
        // 创建游戏列表（使用测试玩家名称）
        GameList gameList = new GameList("测试玩家1", "测试玩家2");
        controller.setGameList(gameList);
        
        // 创建GUI视图和Observer
        guiView = new GUIView(controller);
        
        // 创建并启动游戏循环
        gameLoop = new GUIGameLoop(gameList, commandProvider, guiView);
        
        // 设置Stage属性
        stage.setTitle("棋类游戏 GUI 测试 - 第5阶段集成");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        // 显示窗口
        stage.show();
        
        // 启动游戏循环
        gameLoop.startLoop();
        
        // 初始化UI
        guiView.init(gameList, 1);
        
        System.out.println("GUI应用程序已启动，第5阶段命令集成测试开始");
        
        // 设置关闭处理
        stage.setOnCloseRequest(_ -> {
            if (gameLoop != null) {
                gameLoop.stopLoop();
            }
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
