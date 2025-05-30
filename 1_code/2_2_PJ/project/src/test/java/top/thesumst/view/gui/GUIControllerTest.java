package top.thesumst.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.core.mode.GomokuMode;
import top.thesumst.core.mode.PeaceMode;
import top.thesumst.core.mode.ReversiMode;
import top.thesumst.io.provider.GUICommandProvider;
import top.thesumst.type.component.Player;

/**
 * 第4阶段测试程序 - 测试右侧信息面板的功能
 * 这个测试程序验证：
 * 1. FXML绑定是否正确
 * 2. 三列布局是否正常显示
 * 3. 按钮事件是否能正确处理
 * 4. 游戏列表是否能正常更新
 * 5. 游戏模式切换时UI是否能动态调整
 */
public class GUIControllerTest extends Application {
    
    private GUIController controller;
    private GUICommandProvider commandProvider;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加载FXML文件
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGameView.fxml"));
        BorderPane root = loader.load();
        controller = loader.getController();
        
        // 创建命令提供者
        commandProvider = new GUICommandProvider();
        controller.setCommandProvider(commandProvider);
        
        // 创建测试场景
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("第4阶段测试 - 右侧信息面板");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // 设置测试数据
        setupTestData();
        
        // 模拟命令处理（在后台线程）
        startCommandProcessing();
    }
    
    /**
     * 设置测试数据
     */
    private void setupTestData() {
        try {
            // 创建一个GameList实例（使用已有的构造函数，会自动创建3个游戏）
            GameList gameList = new GameList("测试玩家1", "测试玩家2");
            
            // 设置当前游戏
            GameContainer.switchGameOrder(1);
            
            // 设置GameList引用
            controller.setGameList(gameList);
            
            // 更新界面显示
            controller.updateGameListDisplay();
            controller.updateGameInfoDisplay(GameList.getGame(1));
            
            System.out.println("第4阶段测试 - 测试数据已设置:");
            System.out.println("- 游戏数量: " + GameList.getGameNumber());
            System.out.println("- 游戏1: " + GameList.getGame(1).getGameMode() + "模式");
            System.out.println("- 游戏2: " + GameList.getGame(2).getGameMode() + "模式");
            System.out.println("- 游戏3: " + GameList.getGame(3).getGameMode() + "模式");
            System.out.println("- 当前游戏: 游戏1 (" + GameList.getGame(1).getGameMode() + "模式)");
            
        } catch (Exception e) {
            System.err.println("设置测试数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 启动命令处理（模拟真实的游戏循环）
     */
    private void startCommandProcessing() {
        Thread commandThread = new Thread(() -> {
            System.out.println("第4阶段测试 - 命令处理线程已启动");
            
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (commandProvider.hasCommand()) {
                        commandProvider.getNextCommand();
                        String command = commandProvider.getCurrentInputBuffer();
                        
                        System.out.println("第4阶段测试 - 接收到命令: " + command);
                        
                        // 模拟命令处理
                        handleTestCommand(command);
                        
                        // 清空输入缓冲区
                        commandProvider.setInputBuffer("");
                    }
                    
                    Thread.sleep(100); // 避免过度消耗CPU
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        commandThread.setDaemon(true);
        commandThread.start();
    }
    
    /**
     * 处理测试命令
     */
    private void handleTestCommand(String command) {
        try {
            switch (command) {
                case "1":
                case "2":
                case "3":
                    // 切换游戏
                    int gameNumber = Integer.parseInt(command);
                    if (gameNumber >= 1 && gameNumber <= GameList.getGameNumber()) {
                        GameContainer.switchGameOrder(gameNumber);
                        GameMode currentGame = GameList.getGame(gameNumber);
                        
                        // 更新界面
                        controller.updateGameListDisplay();
                        controller.updateGameInfoDisplay(currentGame);
                        controller.updateChessboardDisplay(currentGame.getSize());
                        
                        System.out.println("第4阶段测试 - 已切换到游戏" + gameNumber + " (" + currentGame.getGameMode() + "模式)");
                    }
                    break;
                    
                case "peace":
                case "reversi":
                case "gomoku":
                    System.out.println("第4阶段测试 - 添加" + command + "游戏的命令已接收");
                    break;
                    
                case "pass":
                    System.out.println("第4阶段测试 - 跳过回合命令已接收");
                    break;
                    
                case "playback gomoku.cmd":
                    System.out.println("第4阶段测试 - 演示模式命令已接收");
                    break;
                    
                case "quit":
                    System.out.println("第4阶段测试 - 退出命令已接收");
                    javafx.application.Platform.exit();
                    break;
                    
                default:
                    if (command.startsWith("@")) {
                        System.out.println("第4阶段测试 - 炸弹命令已接收: " + command);
                    } else {
                        System.out.println("第4阶段测试 - 落子命令已接收: " + command);
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("第4阶段测试 - 处理命令时出错: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 第4阶段测试开始 ===");
        System.out.println("测试项目:");
        System.out.println("1. 右侧信息面板三列布局");
        System.out.println("2. 游戏信息和玩家控制功能");
        System.out.println("3. 游戏列表显示和选择");
        System.out.println("4. 新游戏添加和系统控制按钮");
        System.out.println("5. 按钮事件处理和命令集成");
        System.out.println("6. 不同游戏模式的UI动态调整");
        System.out.println("=========================");
        
        launch(args);
    }
}
