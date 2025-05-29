package top.thesumst.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
// GameContainer import removed - not used
import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
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
    }

    /**
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
}
