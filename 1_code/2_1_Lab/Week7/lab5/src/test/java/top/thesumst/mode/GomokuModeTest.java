package top.thesumst.mode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import top.thesumst.mode.component.*;
import top.thesumst.type.*;
import java.awt.Point;

public class GomokuModeTest {
    private GomokuMode gomokuMode;
    private final int boardSize = 15;
    
    @BeforeEach
    public void setUp() {
        // 初始化五子棋模式，指定游戏参数
        gomokuMode = new GomokuMode(1, "Gomoku", boardSize, "Player1", "Player2", ChessColor.BLACK, ChessColor.WHITE);
        System.out.println("=== 初始化新的五子棋游戏 ===");
        System.out.println("棋盘大小: " + boardSize + "x" + boardSize);
        System.out.println("玩家1: Player1 (黑棋)");
        System.out.println("玩家2: Player2 (白棋)");
    }

    @Test
    public void testBasicMoves() {
        System.out.println("\n=== 测试基本落子操作 ===");
        
        // 测试有效落子
        Point validPoint = new Point(7, 7);
        boolean result = gomokuMode.receiveOperation(validPoint);
        assertTrue(result, "落子应当成功");
        assertEquals(ChessColor.BLACK, gomokuMode.getChessColor(validPoint), "第一步应为黑棋");
        System.out.println("黑棋落子成功: (" + validPoint.x + ", " + validPoint.y + ")");
        
        // 测试落子后切换玩家
        Point validPoint2 = new Point(7, 8);
        result = gomokuMode.receiveOperation(validPoint2);
        assertTrue(result, "落子应当成功");
        assertEquals(ChessColor.WHITE, gomokuMode.getChessColor(validPoint2), "第二步应为白棋");
        System.out.println("白棋落子成功: (" + validPoint2.x + ", " + validPoint2.y + ")");
        
        // 测试重复落子
        System.out.println("尝试在已有棋子位置落子: (" + validPoint.x + ", " + validPoint.y + ")");
        result = gomokuMode.receiveOperation(validPoint);
        assertFalse(result, "重复位置落子应当失败");
        System.out.println("重复落子失败，符合预期");
        
        // 测试越界落子
        // Point invalidPoint = new Point(boardSize, boardSize);
        // System.out.println("尝试在越界位置落子: (" + invalidPoint.x + ", " + invalidPoint.y + ")");
        // result = gomokuMode.receiveOperation(invalidPoint);
        // assertFalse(result, "越界落子应当失败");
        // System.out.println("越界落子失败，符合预期");
    }

    @Test
    public void testHorizontalWin() {
        System.out.println("\n=== 测试水平连线胜利 ===");
        
        // 黑棋在水平方向连成五子
        for (int i = 0; i < 5; i++) {
            // 黑棋落子
            Point blackMove = new Point(i, 5);
            boolean result = gomokuMode.receiveOperation(blackMove);
            assertTrue(result);
            System.out.println("黑棋落子: (" + blackMove.x + ", " + blackMove.y + ")");
            
            if (i < 4) { // 最后一步不需要白棋回应
                // 白棋落子（在另一区域）
                Point whiteMove = new Point(i, 7);
                result = gomokuMode.receiveOperation(whiteMove);
                assertTrue(result);
                System.out.println("白棋落子: (" + whiteMove.x + ", " + whiteMove.y + ")");
            }
        }
        
        // 验证游戏结束且黑方获胜
        assertTrue(gomokuMode.isOver());
        Player winner = gomokuMode.getWinner();
        assertNotNull(winner, "应该有获胜者");
        assertEquals(ChessColor.BLACK, winner.getColor(), "黑方应当获胜");
        System.out.println("游戏结束，获胜者: " + winner.getName() + " (" + (winner.getColor() == ChessColor.BLACK ? "黑棋" : "白棋") + ")");
    }
    
    @Test
    public void testVerticalWin() {
        System.out.println("\n=== 测试垂直连线胜利 ===");
        
        // 白棋在垂直方向连成五子
        // 先让黑棋落一子
        gomokuMode.receiveOperation(new Point(1, 1));
        System.out.println("黑棋落子: (1, 1)");
        
        for (int i = 0; i < 5; i++) {
            // 白棋落子
            Point whiteMove = new Point(5, i);
            boolean result = gomokuMode.receiveOperation(whiteMove);
            assertTrue(result);
            System.out.println("白棋落子: (" + whiteMove.x + ", " + whiteMove.y + ")");
            
            if (i < 4) { // 最后一步不需要黑棋回应
                // 黑棋落子（在另一区域）
                Point blackMove = new Point(7, i);
                result = gomokuMode.receiveOperation(blackMove);
                assertTrue(result);
                System.out.println("黑棋落子: (" + blackMove.x + ", " + blackMove.y + ")");
            }
        }
        
        // 验证游戏结束且白方获胜
        assertTrue(gomokuMode.isOver());
        Player winner = gomokuMode.getWinner();
        assertNotNull(winner);
        assertEquals(ChessColor.WHITE, winner.getColor());
        System.out.println("游戏结束，获胜者: " + winner.getName() + " (" + (winner.getColor() == ChessColor.BLACK ? "黑棋" : "白棋") + ")");
    }
    
    @Test
    public void testDiagonalWin() {
        System.out.println("\n=== 测试对角线连线胜利 ===");
        
        // 黑棋在对角线方向连成五子
        for (int i = 0; i < 5; i++) {
            // 黑棋落子
            Point blackMove = new Point(i, i);
            boolean result = gomokuMode.receiveOperation(blackMove);
            assertTrue(result);
            System.out.println("黑棋落子: (" + blackMove.x + ", " + blackMove.y + ")");
            
            if (i < 4) { // 最后一步不需要白棋回应
                // 白棋落子（在另一区域）
                Point whiteMove = new Point(i, i+2);
                result = gomokuMode.receiveOperation(whiteMove);
                assertTrue(result);
                System.out.println("白棋落子: (" + whiteMove.x + ", " + whiteMove.y + ")");
            }
        }
        
        // 验证游戏结束且黑方获胜
        assertTrue(gomokuMode.isOver());
        Player winner = gomokuMode.getWinner();
        assertNotNull(winner);
        assertEquals(ChessColor.BLACK, winner.getColor());
        System.out.println("游戏结束，获胜者: " + winner.getName() + " (" + (winner.getColor() == ChessColor.BLACK ? "黑棋" : "白棋") + ")");
    }
    
    @Test
    public void testReverseDiagonalWin() {
        System.out.println("\n=== 测试反对角线连线胜利 ===");
        
        // 白棋在反对角线方向连成五子
        // 先让黑棋落一子
        gomokuMode.receiveOperation(new Point(0, 0));
        System.out.println("黑棋落子: (0, 0)");
        
        for (int i = 0; i < 5; i++) {
            // 白棋落子
            Point whiteMove = new Point(i, 4-i);
            boolean result = gomokuMode.receiveOperation(whiteMove);
            assertTrue(result);
            System.out.println("白棋落子: (" + whiteMove.x + ", " + whiteMove.y + ")");
            
            if (i < 4) { // 最后一步不需要黑棋回应
                // 黑棋落子（在另一区域）
                Point blackMove = new Point(i+1, i);
                result = gomokuMode.receiveOperation(blackMove);
                assertTrue(result);
                System.out.println("黑棋落子: (" + blackMove.x + ", " + blackMove.y + ")");
            }
        }
        
        // 验证游戏结束且白方获胜
        assertTrue(gomokuMode.isOver());
        Player winner = gomokuMode.getWinner();
        assertNotNull(winner);
        assertEquals(ChessColor.WHITE, winner.getColor());
        System.out.println("游戏结束，获胜者: " + winner.getName() + " (" + (winner.getColor() == ChessColor.BLACK ? "黑棋" : "白棋") + ")");
    }
    
    @Test
    public void testDraw() {
        System.out.println("\n=== 测试平局情况（棋盘填满） ===");
        
        // 为了简化测试，我们创建一个较小的棋盘
        GomokuMode smallGomoku = new GomokuMode(1, "Gomoku", 3, "Player1", "Player2", ChessColor.BLACK, ChessColor.WHITE);
        System.out.println("创建3x3小棋盘用于平局测试");
        
        // 按特定方式填满棋盘，确保没有五连子
        // 棋盘布局：
        // B W B
        // W B W
        // B W B
        Point[] moves = {
            new Point(0, 0), new Point(0, 1), new Point(0, 2),
            new Point(1, 0), new Point(1, 1), new Point(1, 2),
            new Point(2, 0), new Point(2, 1), new Point(2, 2)
        };
        
        for (Point move : moves) {
            boolean result = smallGomoku.receiveOperation(move);
            assertTrue(result);
            System.out.println(((moves.length - 1 - java.util.Arrays.asList(moves).indexOf(move)) % 2 == 0 ? "黑" : "白") + "棋落子: (" + move.x + ", " + move.y + ")");
        }
        
        // 验证游戏结束，但没有获胜者（平局）
        assertTrue(smallGomoku.isOver());
        assertNull(smallGomoku.getWinner());
        System.out.println("游戏结束，平局（棋盘已满）");
    }
    
    @Test
    public void testStringOperations() {
        System.out.println("\n=== 测试字符串操作 ===");
        
        // 测试退出命令
        boolean result = gomokuMode.receiveOperation("quit");
        assertTrue(result, "quit命令应返回true");
        System.out.println("执行quit命令成功");
        
        // 测试无效命令
        result = gomokuMode.receiveOperation("invalid_command");
        assertFalse(result, "无效命令应返回false");
        System.out.println("执行无效命令失败，符合预期");
    }
}
