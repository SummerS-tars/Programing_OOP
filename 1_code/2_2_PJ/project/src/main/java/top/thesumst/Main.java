package top.thesumst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
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

public class Main extends Application
{
    static GameContainer gameContainer;
    static GameList gameList;
    static GameLoop gameLoop;
    static Observer observer;
    static BaseCommandProvider cmdProvider;        
    
    /**
     * JavaFX Application start方法
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 首先检查是否有存档要加载
        GameSaveData saveData = PersistenceManager.checkAndLoadOnStartup(true);
        
        if (saveData == null) {
            // 显示MOTD对话框
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("欢迎");
            alert.setHeaderText(null);
            alert.setContentText("欢迎来到各种棋类游戏！");
            alert.showAndWait();
        }

        // 设置窗口标题
        primaryStage.setTitle("Board Game");
        
        // 加载FXML文件
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGameView.fxml"));
        Parent root = loader.load();
        
        // 获取FXML加载的GUIController实例
        top.thesumst.view.gui.GUIController guiController = loader.getController();
        
        // 初始化游戏核心组件
        try {
            // 创建Observer - 这里需要将GUIController设置到GUIView中
            observer = ViewFactory.getView("gui");
            
            // 创建CommandProvider
            cmdProvider = CommandProviderFactory.getCommandProvider("gui");
            
            // 如果observer是GUIView的实例，设置controller和commandProvider
            if (observer instanceof top.thesumst.view.gui.GUIView) {
                ((top.thesumst.view.gui.GUIView) observer).setController(guiController);
                
                // 将CommandProvider设置到GUIController中
                if (cmdProvider instanceof top.thesumst.io.provider.GUICommandProvider) {
                    guiController.setCommandProvider((top.thesumst.io.provider.GUICommandProvider) cmdProvider);
                }
            }

            // 创建或恢复游戏状态
            if (saveData != null) {
                // 从存档恢复游戏状态
                System.out.println("正在从存档恢复游戏状态...");
                
                // 使用saveData中已经反序列化的GameList
                // 由于GameList使用静态字段，所以不需要创建新实例
                // 只需使用现有实例的引用
                gameList = saveData.gameList;
                
                // 将GameList设置到GUIController中
                guiController.setGameList(gameList);
                  // 创建游戏循环
                gameLoop = GameLoopFactory.getGameLoop("gui", gameList, cmdProvider, observer);
                
                // 预先设置当前游戏序号，这样GameContainer构造函数就不会默认设为1
                GameContainer.setCurrentGameOrder(saveData.currentGameOrder);
                System.out.println("预设当前游戏序号: " + saveData.currentGameOrder);
                
                // 创建游戏容器
                gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
                
                // 再次确保序号正确（防止构造函数覆盖）
                GameContainer.setCurrentGameOrder(saveData.currentGameOrder);
                
                System.out.println("成功从存档恢复游戏状态，当前游戏序号: " + GameContainer.getCurrentGameOrder());
            }
            else {
                // 创建新的游戏列表
                System.out.println("创建新游戏...");
                gameList = new GameList();
                
                // 将GameList设置到GUIController中
                guiController.setGameList(gameList);
                
                gameLoop = GameLoopFactory.getGameLoop("gui", gameList, cmdProvider, observer);
                gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
            }
            
            // 设置窗口关闭事件处理，添加保存提示
            primaryStage.setOnCloseRequest(event -> {
                event.consume(); // 阻止默认关闭行为
                
                // 询问是否保存游戏
                boolean canClose = PersistenceManager.checkAndSaveOnExit(gameContainer, true);
                
                if (canClose) {
                    if (gameContainer != null) {
                        GameContainer.stopGame();
                    }
                    primaryStage.close();
                    System.exit(0);
                }
            });
            
            // 启动游戏循环（在后台线程中）
            Thread gameThread = new Thread(() -> {
                try {
                    gameContainer.runGame();
                } catch (Exception e) {
                    System.err.println("游戏运行时发生错误: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            gameThread.setDaemon(true);
            gameThread.start();
            
        } catch (Exception e) {
            System.err.println("初始化GUI游戏组件时发生错误: " + e.getMessage());
            e.printStackTrace();
            
            // 显示错误对话框
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("初始化错误");
            errorAlert.setHeaderText("游戏初始化失败");
            errorAlert.setContentText("无法初始化游戏组件: " + e.getMessage());
            errorAlert.showAndWait();
            return;
        }
        
        // 创建Scene并设置到Stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // 设置最小窗口大小
        primaryStage.setMinWidth(1200); // 例如，最小宽度800
        primaryStage.setMinHeight(650); // 例如，最小高度650
        
        // 显示窗口
        primaryStage.show();
    }    /**
     * 游戏主函数
     * @param args
     */
    public static void main(String[] args) 
    {
        // 由于我们现在使用Launcher类作为入口点，此方法只用于JavaFX启动
        // 如果是通过JavaFX的launch方法调用（通常是从Launcher调用的），就执行启动过程
        launch(args);
    }
}
