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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.core.mode.GomokuMode;
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
    private BorderPane mainBorderPane;
    
    @FXML
    private StackPane chessboardContainer;

    @FXML
    private HBox rightInfoPane;
    
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
    
    // ListView选择监听器相关
    private boolean isUpdatingGameList = false; // 标志位，防止ListView更新时触发选择事件

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
        
        // 初始化动态列宽调整
        initializeDynamicColumnWidths();
    }

    /**
     * 初始化棋盘容器
     */
    private void initializeChessboard() {
        chessboardGrid = new GridPane();
        chessboardGrid.setAlignment(javafx.geometry.Pos.CENTER);
        chessboardGrid.setHgap(0);  // 移除棋盘格子间的水平间隙
        chessboardGrid.setVgap(0);  // 移除棋盘格子间的垂直间隙
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
                colLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                chessboardGrid.add(colLabel, col + 1, 0);
            }
            
            // 添加行标签和棋盘按钮
            for (int row = 0; row < boardSize; row++) {
                // 行标签 (1, 2, 3, ... 用16进制表示)
                Label rowLabel = new Label(Integer.toHexString(row + 1).toUpperCase());
                rowLabel.setPrefSize(cellSize, cellSize);
                rowLabel.setAlignment(javafx.geometry.Pos.CENTER);
                rowLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
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
            showFeedbackMessage("炸弹已使用于位置 " + coordinate, false);
        } else {
            inputString = coordinate;
            showFeedbackMessage("棋子落于位置 " + coordinate, false);
        }
        
        // 发送命令到GUICommandProvider
        sendCommand(inputString);
        
        // 输出调试信息
        System.out.println("GUI Click - 棋盘点击: (" + row + ", " + col + ") -> " + inputString);
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
        passButton.setOnAction(_ -> handlePassAction());
        useBombButton.setOnAction(_ -> handleUseBombAction());
        
        // 第二列：游戏列表选择事件
        gameListView.getSelectionModel().selectedItemProperty().addListener(
            (_, _, newValue) -> handleGameListSelection(newValue)
        );
        
        // 第三列：新游戏和系统控制按钮事件
        addPeaceButton.setOnAction(_ -> handleAddGameAction("peace"));
        addReversiButton.setOnAction(_ -> handleAddGameAction("reversi"));
        addGomokuButton.setOnAction(_ -> handleAddGameAction("gomoku"));
        playbackButton.setOnAction(_ -> handlePlaybackAction());
        quitButton.setOnAction(_ -> handleQuitAction());
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
     * 初始化动态列宽调整（第三种方法：程序化计算）
     */
    private void initializeDynamicColumnWidths() {
        // 延迟到下一个JavaFX周期，确保Scene已经完全初始化
        Platform.runLater(() -> {
            // 等待Scene完全初始化
            if (rightInfoPane.getScene() != null) {
                setupColumnWidthCalculation();
            } else {
                // 如果Scene还没有初始化，添加一个监听器等待
                rightInfoPane.sceneProperty().addListener((_, _, newScene) -> {
                    if (newScene != null) {
                        setupColumnWidthCalculation();
                    }
                });
            }
        });
    }

    /**
     * 设置列宽计算逻辑
     */
    private void setupColumnWidthCalculation() {
        // 监听Scene的宽度变化，这样可以响应窗口大小变化
        mainBorderPane.getScene().widthProperty().addListener((_, _, _) -> {
            updateColumnWidths();
        });
        
        // 也监听rightInfoPane的父容器宽度变化
        rightInfoPane.getParent().layoutBoundsProperty().addListener((_, _, _) -> {
            updateColumnWidths();
        });
        
        // 初始化列宽
        updateColumnWidths();
    }

    /**
     * 动态更新列宽（第三种方法：程序化精确计算）
     */
    private void updateColumnWidths() {
        Platform.runLater(() -> {
            // 获取Scene的总宽度
            double sceneWidth = mainBorderPane.getScene().getWidth();
            if (sceneWidth <= 0) {
                return; // 还没有获得有效宽度
            }
            
            // 计算右侧面板的可用宽度
            // Scene总宽度 - 左侧棋盘固定宽度(600) - 一些边距
            double rightPaneAvailableWidth = sceneWidth - 600 - 40; // 40是预留的边距
            
            if (rightPaneAvailableWidth <= 0) {
                return; // 右侧面板宽度不足
            }
            
            // 减去HBox内部间距（spacing = 10, 两个间距 = 20）
            double availableWidth = rightPaneAvailableWidth - 20;
            
            // 计算每列的宽度
            // 第一列和第三列各占35%，第二列（游戏列表）占30%
            double firstColumnWidth = availableWidth * 0.35;
            double secondColumnWidth = availableWidth * 0.30;  // 较窄的游戏列表
            double thirdColumnWidth = availableWidth * 0.35;
            
            // 获取HBox中的所有列（VBox）
            if (rightInfoPane.getChildren().size() >= 3) {
                // 第一列：游戏信息和玩家控制
                if (rightInfoPane.getChildren().get(0) instanceof javafx.scene.layout.VBox firstColumn) {
                    firstColumn.setPrefWidth(firstColumnWidth);
                    firstColumn.setMinWidth(firstColumnWidth);
                    firstColumn.setMaxWidth(firstColumnWidth);
                }
                
                // 第二列：游戏列表
                if (rightInfoPane.getChildren().get(1) instanceof javafx.scene.layout.VBox secondColumn) {
                    secondColumn.setPrefWidth(secondColumnWidth);
                    secondColumn.setMinWidth(secondColumnWidth);
                    secondColumn.setMaxWidth(secondColumnWidth);
                }
                
                // 第三列：新游戏和系统控制
                if (rightInfoPane.getChildren().get(2) instanceof javafx.scene.layout.VBox thirdColumn) {
                    thirdColumn.setPrefWidth(thirdColumnWidth);
                    thirdColumn.setMinWidth(thirdColumnWidth);
                    thirdColumn.setMaxWidth(thirdColumnWidth);
                }
            }
            
            System.out.println(String.format("列宽更新: Scene宽度=%.1f, 右侧可用宽度=%.1f, 第一列=%.1f, 第二列=%.1f, 第三列=%.1f", 
                sceneWidth, rightPaneAvailableWidth, firstColumnWidth, secondColumnWidth, thirdColumnWidth));
        });
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
        // 如果正在更新游戏列表，忽略选择事件
        if (isUpdatingGameList) {
            return;
        }
        
        if (selectedGame != null && !selectedGame.trim().isEmpty()) {
            try {
                // 解析游戏编号（格式："  游戏X [游戏类型]" 或 "> 游戏X [游戏类型]"）
                // 找到"游戏"后面的数字
                int gameIndex = selectedGame.indexOf("游戏");
                if (gameIndex != -1) {
                    String remaining = selectedGame.substring(gameIndex + 2); // "X [游戏类型]"
                    int spaceIndex = remaining.indexOf(' ');
                    if (spaceIndex != -1) {
                        String gameNumberStr = remaining.substring(0, spaceIndex);
                        
                        // 直接发送游戏编号字符串，让InputParser处理
                        sendCommand(gameNumberStr);
                        showFeedbackMessage("正在切换到" + selectedGame, false);
                    } else {
                        showFeedbackMessage("游戏选择失败：格式解析错误", true);
                    }
                } else {
                    showFeedbackMessage("游戏选择失败：未找到游戏编号", true);
                }
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
        // 根据当前游戏类型选择对应的.cmd文件
        String fileName = "peace.cmd"; // 默认文件
        
        if (gameList != null) {
            int currentGameOrder = GameContainer.getCurrentGameOrder();
            if (currentGameOrder > 0 && currentGameOrder <= GameList.getGameNumber()) {
                GameMode currentGame = GameList.getGame(currentGameOrder);
                String gameMode = currentGame.getGameMode();
                
                switch (gameMode) {
                    case "gomoku":
                        fileName = "gomoku.cmd";
                        break;
                    case "reversi":
                        fileName = "reversi.cmd";
                        break;
                    case "peace":
                    default:
                        fileName = "peace.cmd";
                        break;
                }
            }
        }
        
        String command = "playback " + fileName;
        sendCommand(command);
        showFeedbackMessage("开始演示模式: " + fileName, false);
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
            
            // 设置标志位，防止ListView选择事件触发
            isUpdatingGameList = true;
            
            try {
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
            } finally {
                // 重置标志位
                isUpdatingGameList = false;
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
    
    /**
     * 为GUIView提供的UI更新方法
     * 由Observer模式的update方法调用
     */
    public void updateGameUI(top.thesumst.type.Event event, GameList gameList, int currentGameOrder) {
        this.gameList = gameList;
        
        // 更新游戏列表显示
        updateGameListDisplay();
        
        // 更新当前游戏信息
        if (gameList != null && GameList.getGameNumber() > 0 && currentGameOrder >= 1 && currentGameOrder <= GameList.getGameNumber()) {
            GameMode currentGame = GameList.getGame(currentGameOrder);
            updateCurrentGameDisplay(currentGame, currentGameOrder);
            
            // 更新棋盘显示
            ChessBoard board = currentGame.getBoard();
            if (board != null) {
                updateChessboardFromBoard(board);
            }
        }
    }
    
    /**
     * 为GUIView提供的UI初始化方法  
     * 由Observer模式的init方法调用
     */
    public void initializeGameUI(GameList gameList, int currentGameOrder) {
        this.gameList = gameList;
        
        // 初始化游戏列表显示
        updateGameListDisplay();
        
        // 初始化当前游戏显示
        if (gameList != null && GameList.getGameNumber() > 0 && currentGameOrder >= 1 && currentGameOrder <= GameList.getGameNumber()) {
            GameMode currentGame = GameList.getGame(currentGameOrder);
            updateCurrentGameDisplay(currentGame, currentGameOrder);
            
            // 初始化棋盘显示
            ChessBoard board = currentGame.getBoard();
            if (board != null) {
                updateChessboardFromBoard(board);
            }
        }
        
        displayMessage("游戏界面初始化完成");
    }
    
    /**
     * 更新当前游戏信息显示
     */
    private void updateCurrentGameDisplay(GameMode currentGame, int currentGameOrder) {
        // 更新游戏编号
        currentGameNumberLabel.setText("游戏 " + currentGameOrder + " (" + currentGame.getGameMode() + ")");
        
        // 更新玩家信息
        blackPlayerLabel.setText(currentGame.getPlayer1().getName());
        whitePlayerLabel.setText(currentGame.getPlayer2().getName());
        
        // 更新分数（棋子数量）
        ChessBoard board = currentGame.getBoard();
        if (board != null) {
            blackPlayerScoreLabel.setText("棋子: " + board.getChessNumber(ChessStatement.BLACK));
            whitePlayerScoreLabel.setText("棋子: " + board.getChessNumber(ChessStatement.WHITE));
        }
        
        // 更新回合信息
        currentRoundLabel.setText("第 " + currentGame.getTurnNumber() + " 回合 - " + 
                                  (currentGame.isBlackTurn() ? "黑方" : "白方") + "行棋");
        
        // 更新炸弹数量（如果是五子棋模式）
        if ("gomoku".equals(currentGame.getGameMode())) {
            try {
                // 使用反射或转换来获取炸弹数量，如果Player没有getBombNumber方法
                blackBombsLabel.setText("炸弹: " + 0); // 暂时使用默认值
                whiteBombsLabel.setText("炸弹: " + 0); // 暂时使用默认值
            } catch (Exception e) {
                blackBombsLabel.setText("炸弹: N/A");
                whiteBombsLabel.setText("炸弹: N/A");
            }
        } else {
            blackBombsLabel.setText("炸弹: N/A");
            whiteBombsLabel.setText("炸弹: N/A");
        }
        
        // 更新按钮显示状态
        updateButtonVisibility(currentGame.getGameMode(), !currentGame.isOver());
    }
    
    /**
     * 从ChessBoard更新棋盘显示
     */
    private void updateChessboardFromBoard(ChessBoard board) {
        if (board != null && chessboardButtons != null) {
            int boardSize = currentBoardSize; // 使用当前棋盘大小，而不是从board获取
            
            // 更新每个位置的显示
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    if (chessboardButtons[row][col] != null) {
                        Point point = new Point(row, col);
                        ChessStatement statement = board.getChessStatement(point);
                        updateChessboardCell(row, col, statement);
                    }
                }
            }
        }
    }
    
    /**
     * 更新单个棋盘格子的显示
     */
    private void updateChessboardCell(int row, int col, ChessStatement statement) {
        Button button = chessboardButtons[row][col];
        
        // 清除之前的图形
        button.setGraphic(null);
        button.setText("");
        
        // 根据棋子状态设置显示
        switch (statement) {
            case BLACK:
                Circle blackPiece = new Circle(12, Color.BLACK);
                button.setGraphic(blackPiece);
                break;
            case WHITE:
                Circle whitePiece = new Circle(12, Color.WHITE);
                whitePiece.setStroke(Color.BLACK);
                whitePiece.setStrokeWidth(1.5);
                button.setGraphic(whitePiece);
                break;
            case VALID:
                button.setText("+");
                button.setStyle(button.getStyle() + "; -fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 18px;");
                break;
            case BARRIER:
                Rectangle barrier = new Rectangle(20, 20, Color.BROWN);
                button.setGraphic(barrier);
                break;
            case BOMBED:
                button.setText("@");
                button.setStyle(button.getStyle() + "; -fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 18px;");
                break;
            case BLANK:
            default:
                // 恢复默认样式
                button.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #8B4513; -fx-border-width: 1px;");
                break;
        }
    }
    
    /**
     * 更新按钮可见性
     */
    private void updateButtonVisibility(String gameMode, boolean gameIsActive) {
        Platform.runLater(() -> {
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
    
    /**
     * 显示消息给用户
     */
    public void displayMessage(String message) {
        if (feedbackMessageLabel != null) {
            feedbackMessageLabel.setText(message);
            feedbackMessageLabel.setTextFill(Color.BLUE);
        }
    }
    
    /**
     * 显示错误消息给用户
     */
    public void displayErrorMessage(String errorMessage) {
        if (feedbackMessageLabel != null) {
            feedbackMessageLabel.setText("错误: " + errorMessage);
            feedbackMessageLabel.setTextFill(Color.RED);
        }
    }
}
