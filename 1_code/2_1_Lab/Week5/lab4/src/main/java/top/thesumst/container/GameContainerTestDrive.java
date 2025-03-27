package top.thesumst.container;

import top.thesumst.mode.*;
import top.thesumst.tools.*;
import top.thesumst.tools.command.*;
import top.thesumst.type.ChessColor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * GameContainer测试驱动类
 * 用于测试GameContainer的各项功能，包括完整的游戏流程
 */
public class GameContainerTestDrive {

    /**
     * 测试和平模式的完整游戏流程
     * @param gameList 游戏列表
     * @param peaceModeOrder 和平模式游戏序号
     * @throws InterruptedException
     */
    private static void testPeaceModeComplete(GameList gameList, int peaceModeOrder) throws InterruptedException {
        System.out.println("\n=== 测试和平模式完整游戏流程 ===");
        GameContainer.switchGameOrder(peaceModeOrder);
        GameMode peaceGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        
        // 确保是和平模式
        if (!(peaceGame instanceof PeaceMode)) {
            System.out.println("错误：无法测试和平模式，当前游戏不是和平模式");
            return;
        }
        
        PrintTools.clearConsole();
        PrintTools.printBoard(peaceGame);
        PrintTools.printPlayerInfo(peaceGame);
        PrintTools.printGameList(gameList);
        System.out.println("开始和平模式完整游戏测试 - 游戏序号: " + GameContainer.getCurrentGameOrder());
        Thread.sleep(1000);
        
        // 和平模式中，依次填满整个棋盘即可
        int boardSize = 8; // 默认棋盘大小
        int totalMoves = 0;
        
        // 系统地填满棋盘
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Point movePoint = new Point(i, j);
                GoCommand goCmd = new GoCommand(movePoint);
                CommandResult result = goCmd.execute(peaceGame, gameList);
                
                if (result.isSuccess()) {
                    totalMoves++;
                    
                    PrintTools.clearConsole();
                    PrintTools.printBoard(peaceGame);
                    PrintTools.printPlayerInfo(peaceGame);
                    PrintTools.printGameList(gameList);
                    System.out.println("和平模式第 " + totalMoves + " 步 - 位置(" + i + "," + j + "): " + result.getMessage());
                    
                    // 检查游戏是否结束
                    if (peaceGame.isOver()) {
                        System.out.println("和平模式游戏结束！黑棋数: " + peaceGame.getBoard().getChessNumber(ChessColor.BLACK) + 
                                         ", 白棋数: " + peaceGame.getBoard().getChessNumber(ChessColor.WHITE));
                        Thread.sleep(1000);
                        break;
                    }
                    
                    Thread.sleep(150); // 稍微暂停以便观察
                }
            }
            
            if (peaceGame.isOver()) {
                break;
            }
        }
        
        // 测试游戏结束后尝试下棋
        System.out.println("\n测试游戏结束后尝试下棋:");
        GoCommand postGameCmd = new GoCommand(new Point(0, 0));
        CommandResult postGameResult = postGameCmd.execute(peaceGame, gameList);
        System.out.println("游戏结束后下棋结果: " + postGameResult.getMessage());
        Thread.sleep(1000);
    }
    
    /**
     * 测试翻转棋模式的完整游戏流程
     * @param gameList 游戏列表
     * @param reversiModeOrder 翻转棋模式游戏序号
     * @throws InterruptedException
     */
    private static void testReversiModeComplete(GameList gameList, int reversiModeOrder) throws InterruptedException {
        System.out.println("\n=== 测试翻转棋模式完整游戏流程 ===");
        GameContainer.switchGameOrder(reversiModeOrder);
        GameMode reversiGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        
        // 确保是翻转棋模式
        if (!(reversiGame instanceof ReversiMode)) {
            System.out.println("错误：无法测试翻转棋模式，当前游戏不是翻转棋模式");
            return;
        }
        
        PrintTools.clearConsole();
        PrintTools.printBoard(reversiGame);
        PrintTools.printPlayerInfo(reversiGame);
        PrintTools.printGameList(gameList);
        System.out.println("开始翻转棋模式完整游戏测试 - 游戏序号: " + GameContainer.getCurrentGameOrder());
        Thread.sleep(1000);
        
        // 翻转棋位置策略 - 这些是常见的有效位置
        List<Point> potentialMoves = new ArrayList<>();
        // 初始可行的四个位置
        potentialMoves.add(new Point(2, 3));
        potentialMoves.add(new Point(3, 2));
        potentialMoves.add(new Point(4, 5));
        potentialMoves.add(new Point(5, 4));
        
        // 其他可能的位置
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                potentialMoves.add(new Point(i, j));
            }
        }
        
        int moveCount = 0;
        int passCount = 0;
        boolean gameEnded = false;
        
        // 尝试最多100步，防止无限循环
        while (moveCount + passCount < 100 && !gameEnded) {
            boolean validMoveMade = false;
            
            // 尝试所有潜在位置
            for (Point move : potentialMoves) {
                GoCommand goCmd = new GoCommand(move);
                CommandResult result = goCmd.execute(reversiGame, gameList);
                
                if (result.isSuccess()) {
                    moveCount++;
                    validMoveMade = true;
                    
                    PrintTools.clearConsole();
                    PrintTools.printBoard(reversiGame);
                    PrintTools.printPlayerInfo(reversiGame);
                    PrintTools.printGameList(gameList);
                    System.out.println("翻转棋第 " + moveCount + " 步 - 位置(" + move.x + "," + move.y + "): " + result.getMessage());
                    Thread.sleep(500);
                    
                    // 检查游戏是否结束
                    if (reversiGame.isOver()) {
                        System.out.println("翻转棋游戏结束！黑棋数: " + reversiGame.getBoard().getChessNumber(ChessColor.BLACK) + 
                                         ", 白棋数: " + reversiGame.getBoard().getChessNumber(ChessColor.WHITE));
                        gameEnded = true;
                        break;
                    }
                    
                    break; // 找到有效位置后跳出循环
                }
            }
            
            // 如果没有有效位置，尝试Pass
            if (!validMoveMade && !gameEnded) {
                PassCommand passCmd = new PassCommand();
                CommandResult passResult = passCmd.execute(reversiGame, gameList);
                passCount++;
                
                PrintTools.clearConsole();
                PrintTools.printBoard(reversiGame);
                PrintTools.printPlayerInfo(reversiGame);
                PrintTools.printGameList(gameList);
                System.out.println("翻转棋 Pass " + passCount + " 次: " + passResult.getMessage());
                Thread.sleep(500);
                
                // 连续两次Pass通常表示游戏结束
                if (passCount >= 2) {
                    System.out.println("连续两次Pass，游戏应该结束");
                    gameEnded = true;
                }
            } else {
                // 重置Pass计数
                passCount = 0;
            }
        }
        
        // 测试游戏结束后的状态
        if (gameEnded) {
            System.out.println("\n测试游戏结束后尝试下棋:");
            GoCommand postGameCmd = new GoCommand(new Point(0, 0));
            CommandResult postGameResult = postGameCmd.execute(reversiGame, gameList);
            System.out.println("游戏结束后下棋结果: " + postGameResult.getMessage());
        } else {
            System.out.println("达到最大步数限制，测试结束");
        }
        Thread.sleep(1000);
    }
    
    /**
     * 测试游戏容器的基本功能
     * @param gameContainer 游戏容器
     * @param gameList 游戏列表
     * @throws InterruptedException
     */
    private static void testBasicFeatures(GameContainer gameContainer, GameList gameList) throws InterruptedException {
        System.out.println("=== 测试游戏容器基本功能 ===");
        
        // 创建新游戏测试
        System.out.println("\n测试创建新游戏:");
        GameMode currentGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        NewGameCommand newPeaceCmd = new NewGameCommand("peace");
        CommandResult newPeaceResult = newPeaceCmd.execute(currentGame, gameList);
        // 切换到新创建的游戏
        int newGameOrder = GameList.getGameNumber();
        GameContainer.switchGameOrder(newGameOrder);
        currentGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        
        PrintTools.clearConsole();
        PrintTools.printBoard(currentGame);
        PrintTools.printPlayerInfo(currentGame);
        PrintTools.printGameList(gameList);
        System.out.println("创建新的和平模式: " + newPeaceResult.getMessage());
        System.out.println("当前游戏数量: " + GameList.getGameNumber() + ", 当前游戏序号: " + GameContainer.getCurrentGameOrder());
        Thread.sleep(1000);
        
        // 创建新的翻转棋游戏
        NewGameCommand newReversiCmd = new NewGameCommand("reversi");
        CommandResult newReversiResult = newReversiCmd.execute(currentGame, gameList);
        // 切换到新创建的游戏
        newGameOrder = GameList.getGameNumber();
        GameContainer.switchGameOrder(newGameOrder);
        currentGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        
        PrintTools.clearConsole();
        PrintTools.printBoard(currentGame);
        PrintTools.printPlayerInfo(currentGame);
        PrintTools.printGameList(gameList);
        System.out.println("创建新的翻转棋模式: " + newReversiResult.getMessage());
        System.out.println("当前游戏数量: " + GameList.getGameNumber() + ", 当前游戏序号: " + GameContainer.getCurrentGameOrder());
        Thread.sleep(1000);
        
        // 测试游戏切换
        System.out.println("\n测试游戏切换:");
        GameContainer.switchGameOrder(1);
        currentGame = gameList.getGame(GameContainer.getCurrentGameOrder());
        
        PrintTools.clearConsole();
        PrintTools.printBoard(currentGame);
        PrintTools.printPlayerInfo(currentGame);
        PrintTools.printGameList(gameList);
        System.out.println("已切换到游戏序号 1");
        Thread.sleep(1000);
    }
    
    public static void main(String[] args) {
        System.out.println("===== GameContainer 自动测试开始 =====");
        
        try {
            // 创建游戏容器
            GameContainer gameContainer = new GameContainer();
            GameList gameList = gameContainer.getGameList();
            
            // 测试基本功能
            testBasicFeatures(gameContainer, gameList);
            
            // 测试和平模式完整游戏
            // 初始默认应该是和平模式为1
            testPeaceModeComplete(gameList, 1);
            
            // 测试翻转棋模式完整游戏
            // 初始默认应该是翻转棋为2
            testReversiModeComplete(gameList, 2);
            
            System.out.println("\n===== GameContainer 自动测试完成 =====");
            
        } catch (Exception e) {
            System.out.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}