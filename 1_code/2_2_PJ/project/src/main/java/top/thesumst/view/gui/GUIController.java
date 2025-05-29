package top.thesumst.view.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.core.mode.GomokuMode;
import top.thesumst.io.input.InputParser;
import top.thesumst.io.input.InputResult;
import top.thesumst.io.input.InputType;
import top.thesumst.io.provider.GUICommandProvider;
import top.thesumst.type.ChessStatement;
import top.thesumst.type.component.ChessBoard;

import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI控制器类，负责处理MainGameView.fxml的UI控件
 */
public class GUIController implements Initializable {    
    @FXML
    private StackPane chessboardContainer;

    @FXML
    private GridPane rightInfoPane;
    
    // 第一列：游戏信息和玩家控制的FXML绑定
    @FXML
    private Label currentGameNumberLabel;
    
    @FXML
    private Label blackPlayerLabel;
    
    @FXML
    private Label blackPlayerScoreLabel;
    
    @FXML
    private Label whitePlayerLabel;
    
    @FXML
    private Label whitePlayerScoreLabel;
    
    @FXML
    private Label currentRoundLabel;
    
    @FXML
    private Label blackBombsLabel;
    
    @FXML
    private Label whiteBombsLabel;
    
    @FXML
    private Button passButton;
    
    @FXML
    private Button useBombButton;
    
    // 第二列：游戏列表的FXML绑定
    @FXML
    private ListView<String> gameListView;
    
    // 第三列：新游戏和系统控制的FXML绑定
    @FXML
    private Button addPeaceButton;
    
    @FXML
    private Button addReversiButton;
    
    @FXML
    private Button addGomokuButton;
    
    @FXML
    private Button playbackButton;
    
    @FXML
    private Button quitButton;
    
    // 反馈消息标签
    @FXML
    private Label feedbackMessageLabel;

    // 棋盘相关的实例变量
    private GridPane chessboardGrid;
    private Button[][] chessboardButtons;
    private int currentBoardSize = 8; // 默认8x8，将根据当前游戏动态调整
    private boolean isBombModeActive = false;

    // 用于访问游戏状态的引用（后续阶段会通过依赖注入或其他方式设置）
    private GameList gameList;
    private GUICommandProvider commandProvider;

    /**
     * 初始化方法，在FXML加载完成后自动调用
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化棋盘显示
        initializeChessboard();
        
        // 创建一个默认的8x8棋盘用于演示
        updateChessboardDisplay(8);
        
        // 初始化事件处理器
        initializeEventHandlers();
        
        // 初始化游戏列表
        initializeGameListView();
        
        // 初始化UI状态
        updateUIForGameMode(null);
    }

    /**
     * 初始化棋盘容器
     */
    private void initializeChessboard() {
        chessboardGrid = new GridPane();
        chessboardGrid.setAlignment(javafx.geometry.Pos.CENTER);
        chessboardGrid.setHgap(1);
        chessboardGrid.setVgap(1);
        chessboardContainer.getChildren().clear();
        chessboardContainer.getChildren().add(chessboardGrid);
    }

    /**
     * 更新棋盘显示
     * @param boardSize 棋盘大小 (8 for 8x8, 15 for 15x15)
     */
    public void updateChessboardDisplay(int boardSize) {
        Platform.runLater(() -> {
            this.currentBoardSize = boardSize;
            chessboardGrid.getChildren().clear();
            
            // 计算单元格大小，在600x600容器中留出标签空间
            double availableSize = 560; // 留出40像素给行列标签
            double cellSize = availableSize / (boardSize + 1); // +1 for labels
            
            // 创建按钮数组
            chessboardButtons = new Button[boardSize][boardSize];
            
            // 添加列标签 (A, B, C, ...)
            for (int col = 0; col < boardSize; col++) {
                Label colLabel = new Label(String.valueOf((char)('A' + col)));
                colLabel.setPrefSize(cellSize, cellSize);
                colLabel.setAlignment(javafx.geometry.Pos.CENTER);
                colLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
                chessboardGrid.add(colLabel, col + 1, 0);
            }
            
            // 添加行标签和棋盘按钮
            for (int row = 0; row < boardSize; row++) {
                // 行标签 (1, 2, 3, ... 用16进制表示)
                Label rowLabel = new Label(Integer.toHexString(row + 1).toUpperCase());
                rowLabel.setPrefSize(cellSize, cellSize);
                rowLabel.setAlignment(javafx.geometry.Pos.CENTER);
                rowLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
                chessboardGrid.add(rowLabel, 0, row + 1);
                
                // 棋盘按钮
                for (int col = 0; col < boardSize; col++) {
                    Button cellButton = createChessboardButton(row, col, cellSize);
                    chessboardButtons[row][col] = cellButton;
                    chessboardGrid.add(cellButton, col + 1, row + 1);
                }
            }
        });
    }

    /**
     * 创建棋盘单元格按钮
     */
    private Button createChessboardButton(int row, int col, double cellSize) {
        Button button = new Button();
        button.setPrefSize(cellSize, cellSize);
        button.setMinSize(cellSize, cellSize);
        button.setMaxSize(cellSize, cellSize);
        
        // 设置按钮样式
        button.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #8B4513; -fx-border-width: 1px;");
        
        // 添加点击事件处理
        button.setOnAction(_ -> handleChessboardClick(row, col));
        
        return button;
    }    /**
     * 处理棋盘点击事件
     */
    private void handleChessboardClick(int row, int col) {
        // 转换坐标为InputParser格式
        String coordinate = convertToInputFormat(row, col);
        
        // 创建输入字符串
        String inputString;
        if (isBombModeActive) {
            inputString = "@" + coordinate; // 炸弹格式为 @1A
            isBombModeActive = false; // 使用炸弹后重置状态
        } else {
            inputString = coordinate;
        }
        
        // 解析输入并输出到控制台（用于测试）
        try {
            InputResult inputResult = InputParser.parse(inputString);
            System.out.println("GUI Click - 原始输入: " + inputString);
            System.out.println("GUI Click - 解析结果: " + inputResult);
            System.out.println("GUI Click - 输入类型: " + inputResult.getType());
            
            // 获取坐标信息（如果是落子或炸弹命令）
            if (inputResult.getType() == InputType.CHESS_MOVE || inputResult.getType() == InputType.USE_BOMB) {
                Point pos = inputResult.getDataAs(Point.class);
                System.out.println("GUI Click - 坐标: (" + pos.x + ", " + pos.y + ")");
            }
              // 集成GUICommandProvider处理命令
            if (commandProvider != null) {
                commandProvider.setInputBuffer(inputString);
                System.out.println("GUI Click - 命令已设置到CommandProvider");
            } else {
                System.out.println("GUI Click - CommandProvider未设置，无法处理命令");
            }
            
            // 检查当前游戏状态（如果gameList可用）
            if (gameList != null) {
                // 使用GameContainer获取当前游戏
                int currentGameOrder = GameContainer.getCurrentGameOrder();
                if (currentGameOrder > 0 && currentGameOrder <= GameList.getGameNumber()) {
                    GameMode currentGame = GameList.getGame(currentGameOrder);
                    System.out.println("GUI Click - 当前游戏: " + currentGame.getClass().getSimpleName());
                    System.out.println("GUI Click - 棋盘大小: " + currentGame.getSize());
                    
                    // 如果棋盘大小发生变化，更新显示
                    if (currentGame.getSize() != currentBoardSize) {
                        updateChessboardDisplay(currentGame.getSize());
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("GUI Click - 解析错误: " + e.getMessage());
        }
    }

    /**
     * 将行列坐标转换为InputParser格式
     * @param row 行索引 (0-based)
     * @param col 列索引 (0-based)
     * @return 格式化的坐标字符串，如 "1A", "8H" 等
     */
    private String convertToInputFormat(int row, int col) {
        // 行：转换为16进制 (1-based)
        String rowStr = Integer.toHexString(row + 1).toUpperCase();
        // 列：转换为字母 (A-based)
        char colChar = (char)('A' + col);
        return rowStr + colChar;
    }

    /**
     * 更新棋盘上的棋子显示
     * @param gameMode 当前游戏模式
     */
    public void updateChessPieces(GameMode gameMode) {
        if (gameMode == null || chessboardButtons == null) {
            return;
        }
        
        Platform.runLater(() -> {
            ChessBoard board = gameMode.getBoard();
            int boardSize = gameMode.getSize();
            
            // 如果棋盘大小发生变化，重新创建棋盘
            if (boardSize != currentBoardSize) {
                updateChessboardDisplay(boardSize);
                return;
            }
            
            // 更新每个位置的棋子显示
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    Button button = chessboardButtons[row][col];
                    Point point = new Point(row, col);
                    ChessStatement statement = board.getChessStatement(point);
                    
                    // 清除之前的图形
                    button.setGraphic(null);
                    button.setText("");
                    
                    // 根据棋子状态设置显示
                    switch (statement) {
                        case BLACK:
                            Circle blackPiece = new Circle(8, Color.BLACK);
                            button.setGraphic(blackPiece);
                            break;
                        case WHITE:
                            Circle whitePiece = new Circle(8, Color.WHITE);
                            whitePiece.setStroke(Color.BLACK);
                            whitePiece.setStrokeWidth(1);
                            button.setGraphic(whitePiece);
                            break;
                        case VALID:
                            button.setText("+");
                            button.setStyle(button.getStyle() + "; -fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case BARRIER:
                            Rectangle barrier = new Rectangle(16, 16, Color.BROWN);
                            button.setGraphic(barrier);
                            break;
                        case BOMBED:
                            button.setText("@");
                            button.setStyle(button.getStyle() + "; -fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        case BLANK:
                        default:
                            // 恢复默认样式
                            button.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #8B4513; -fx-border-width: 1px;");
                            break;
                    }
                }
            }
        });
    }

    /**
     * 设置炸弹模式状态
     * @param active 是否激活炸弹模式
     */
    public void setBombModeActive(boolean active) {
        this.isBombModeActive = active;
    }    /**
     * 设置游戏列表引用（后续阶段使用）
     */
    public void setGameList(GameList gameList) {
        this.gameList = gameList;
    }

    /**
     * 设置命令提供者引用（后续阶段使用）
     */
    public void setCommandProvider(GUICommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    /**
     * 初始化所有事件处理器
     */
    private void initializeEventHandlers() {
        // 第一列：玩家控制按钮事件
        passButton.setOnAction(e -> handlePassAction());
        useBombButton.setOnAction(e -> handleUseBombAction());
        
        // 第二列：游戏列表选择事件
        gameListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> handleGameListSelection(newValue)
        );
        
        // 第三列：新游戏和系统控制按钮事件
        addPeaceButton.setOnAction(e -> handleAddGameAction("peace"));
        addReversiButton.setOnAction(e -> handleAddGameAction("reversi"));
        addGomokuButton.setOnAction(e -> handleAddGameAction("gomoku"));
        playbackButton.setOnAction(e -> handlePlaybackAction());
        quitButton.setOnAction(e -> handleQuitAction());
    }

    /**
     * 初始化游戏列表视图
     */
    private void initializeGameListView() {
        // 创建观察列表用于游戏列表
        ObservableList<String> gameItems = FXCollections.observableArrayList();
        gameListView.setItems(gameItems);
    }

    /**
     * 处理跳过回合动作
     */
    private void handlePassAction() {
        sendCommand("pass");
        showFeedbackMessage("已跳过本回合", false);
    }

    /**
     * 处理使用炸弹动作
     */
    private void handleUseBombAction() {
        setBombModeActive(true);
        showFeedbackMessage("炸弹模式已激活，请点击要炸毁的位置", false);
    }

    /**
     * 处理游戏列表选择
     */
    private void handleGameListSelection(String selectedGame) {
        if (selectedGame != null && !selectedGame.trim().isEmpty()) {
            try {
                // 解析游戏编号（格式："游戏X [游戏类型]"）
                String gameNumberStr = selectedGame.substring(2, selectedGame.indexOf(' ', 2));
                int gameNumber = Integer.parseInt(gameNumberStr);
                
                sendCommand(String.valueOf(gameNumber));
                showFeedbackMessage("已切换到" + selectedGame, false);
            } catch (Exception e) {
                showFeedbackMessage("游戏选择失败：" + e.getMessage(), true);
            }
        }
    }

    /**
     * 处理添加新游戏动作
     */
    private void handleAddGameAction(String gameMode) {
        sendCommand(gameMode);
        showFeedbackMessage("正在添加" + gameMode + "游戏...", false);
    }

    /**
     * 处理演示模式动作
     */
    private void handlePlaybackAction() {
        // 暂时使用默认的示例脚本
        sendCommand("playback gomoku.cmd");
        showFeedbackMessage("开始演示模式...", false);
    }

    /**
     * 处理退出游戏动作
     */
    private void handleQuitAction() {
        sendCommand("quit");
        showFeedbackMessage("正在退出游戏...", false);
    }

    /**
     * 发送命令到GUICommandProvider
     */
    private void sendCommand(String command) {
        if (commandProvider != null) {
            commandProvider.offerCommand(command);
            System.out.println("GUI Action - 命令已发送: " + command);
        } else {
            System.out.println("GUI Action - CommandProvider未设置，无法发送命令: " + command);
        }
    }

    /**
     * 显示反馈消息
     */
    private void showFeedbackMessage(String message, boolean isError) {
        Platform.runLater(() -> {
            feedbackMessageLabel.setText(message);
            if (isError) {
                feedbackMessageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                feedbackMessageLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            }
        });
    }

    /**
     * 更新游戏列表显示
     */
    public void updateGameListDisplay() {
        Platform.runLater(() -> {
            if (gameList == null) {
                return;
            }
            
            ObservableList<String> gameItems = gameListView.getItems();
            gameItems.clear();
            
            int currentGameOrder = GameContainer.getCurrentGameOrder();
            
            for (int i = 1; i <= GameList.getGameNumber(); i++) {
                GameMode game = GameList.getGame(i);
                String prefix = (i == currentGameOrder) ? "> " : "  ";
                String gameInfo = String.format("%s游戏%d [%s]", prefix, i, game.getGameMode());
                gameItems.add(gameInfo);
            }
            
            // 选中当前游戏
            if (currentGameOrder > 0 && currentGameOrder <= gameItems.size()) {
                gameListView.getSelectionModel().select(currentGameOrder - 1);
            }
        });
    }

    /**
     * 更新游戏信息显示
     */
    public void updateGameInfoDisplay(GameMode currentGame) {
        Platform.runLater(() -> {
            if (currentGame == null) {
                return;
            }
            
            int currentGameOrder = GameContainer.getCurrentGameOrder();
            currentGameNumberLabel.setText("游戏" + currentGameOrder);
            
            // 获取玩家信息
            blackPlayerLabel.setText("黑棋 [" + currentGame.getPlayer1().getName() + "]");
            whitePlayerLabel.setText("白棋 [" + currentGame.getPlayer2().getName() + "]");
            
            // 更新UI元素可见性基于游戏模式
            updateUIForGameMode(currentGame);
            
            // 根据游戏类型显示分数或炸弹信息
            String gameMode = currentGame.getGameMode();
            switch (gameMode) {
                case "reversi":
                    // Reversi模式：使用getChessNumber()作为分数
                    blackPlayerScoreLabel.setText("分数: " + currentGame.getPlayer1().getChessNumber());
                    whitePlayerScoreLabel.setText("分数: " + currentGame.getPlayer2().getChessNumber());
                    blackPlayerScoreLabel.setVisible(true);
                    whitePlayerScoreLabel.setVisible(true);
                    blackBombsLabel.setVisible(false);
                    whiteBombsLabel.setVisible(false);
                    break;
                case "gomoku":
                    // Gomoku模式：使用GomokuMode的getBombNumber()方法
                    if (currentGame instanceof GomokuMode) {
                        GomokuMode gomokuGame = (GomokuMode) currentGame;
                        blackBombsLabel.setText("黑方炸弹: " + gomokuGame.getBombNumber(currentGame.getPlayer1()));
                        whiteBombsLabel.setText("白方炸弹: " + gomokuGame.getBombNumber(currentGame.getPlayer2()));
                    } else {
                        blackBombsLabel.setText("黑方炸弹: 0");
                        whiteBombsLabel.setText("白方炸弹: 0");
                    }
                    blackBombsLabel.setVisible(true);
                    whiteBombsLabel.setVisible(true);
                    blackPlayerScoreLabel.setVisible(false);
                    whitePlayerScoreLabel.setVisible(false);
                    break;
                case "peace":
                default:
                    blackPlayerScoreLabel.setVisible(false);
                    whitePlayerScoreLabel.setVisible(false);
                    blackBombsLabel.setVisible(false);
                    whiteBombsLabel.setVisible(false);
                    break;
            }
            
            // 显示回合信息
            if (!currentGame.isOver()) {
                String currentPlayerName = currentGame.getCurrentPlayer().getName();
                String currentColor = currentGame.getCurrentPlayer().getColor() == ChessStatement.BLACK ? "黑棋" : "白棋";
                currentRoundLabel.setText("当前回合: " + currentColor + " (" + currentPlayerName + ")");
                currentRoundLabel.setVisible(true);
            } else {
                currentRoundLabel.setText("游戏已结束");
                currentRoundLabel.setVisible(true);
            }
        });
    }

    /**
     * 根据游戏模式更新UI元素的可见性
     */
    private void updateUIForGameMode(GameMode currentGame) {
        Platform.runLater(() -> {
            if (currentGame == null) {
                // 如果没有当前游戏，隐藏所有游戏相关的控件
                passButton.setVisible(false);
                useBombButton.setVisible(false);
                currentRoundLabel.setVisible(false);
                blackPlayerScoreLabel.setVisible(false);
                whitePlayerScoreLabel.setVisible(false);
                blackBombsLabel.setVisible(false);
                whiteBombsLabel.setVisible(false);
                return;
            }
            
            String gameMode = currentGame.getGameMode();
            boolean gameIsActive = !currentGame.isOver();
            
            switch (gameMode) {
                case "reversi":
                    passButton.setVisible(gameIsActive);
                    useBombButton.setVisible(false);
                    break;
                case "gomoku":
                    passButton.setVisible(false);
                    useBombButton.setVisible(gameIsActive);
                    break;
                case "peace":
                default:
                    passButton.setVisible(false);
                    useBombButton.setVisible(false);
                    break;
            }
        });
    }
}
