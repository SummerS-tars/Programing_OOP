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
import top.thesumst.view.gui.GUIController;
import top.thesumst.view.gui.GUIView;

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
        // 首先显示MOTD对话框
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("欢迎");
        alert.setHeaderText(null);
        alert.setContentText("欢迎来到各种棋类游戏！" + System.lineSeparator() + System.lineSeparator() + "将来这里会提示是否加载存在的游戏存档。"); // 修改MOTD内容并使用System.lineSeparator()
        alert.showAndWait();

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
            
            gameList = new GameList();
            
            // 将GameList设置到GUIController中
            guiController.setGameList(gameList);
            
            gameLoop = GameLoopFactory.getGameLoop("gui", gameList, cmdProvider, observer);
            gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
            
            // 设置窗口关闭事件处理
            primaryStage.setOnCloseRequest(_ -> {
                if (gameContainer != null) {
                    GameContainer.stopGame();
                }
                System.exit(0);
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
    }

    /**
     * 游戏主函数
     * @param args
     */
    public static void main(String[] args) 
    {
        // 检查启动参数，如果是GUI模式则启动JavaFX应用
        if (args.length > 0 && "gui".equals(args[0])) {
            // 启动JavaFX应用
            launch(args);
        } else {
            // 传统CLI模式
            CLIPrintTools.gameMotd();
            try{
                observer = ViewFactory.getView(args[0]);
                cmdProvider = CommandProviderFactory.getCommandProvider(args[0]);
                gameList = new GameList();
                gameLoop = GameLoopFactory.getGameLoop(args[0], gameList, cmdProvider, observer);
                gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
                gameContainer.runGame();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("启动模式参数错误: " + e.getMessage());
                System.out.println("请使用 'cli' 或 'gui' 启动游戏");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("发生了未知错误: " + e.getMessage());
            }
        }
    }
}
