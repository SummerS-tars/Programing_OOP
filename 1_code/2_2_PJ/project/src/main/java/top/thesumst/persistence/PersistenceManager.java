package top.thesumst.persistence;

import top.thesumst.core.container.GameContainer;
import top.thesumst.tools.ScannerTools;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.util.Scanner;

/**
 * 游戏状态持久化管理器
 * 提供高级的保存和加载功能
 */
public class PersistenceManager {
    
    private static final int MAX_AUTOSAVE_COUNT = 10;
    private static final String AUTOSAVE_PREFIX = "autosave_";
    private static final String QUICKSAVE_NAME = "quicksave";
    
    /**
     * 快速保存游戏状态
     * @param gameContainer 游戏容器
     * @throws IOException 保存失败时抛出
     */
    public static void quickSave(GameContainer gameContainer) throws IOException {
        GameStateSerializer.saveGameState(gameContainer, QUICKSAVE_NAME);
    }
    
    /**
     * 快速加载游戏状态
     * @return 加载的游戏保存数据，如果没有快速保存则返回null
     * @throws IOException 加载失败时抛出
     */
    public static GameSaveData quickLoad() throws IOException {
        String[] saveFiles = GameStateSerializer.getSaveFileList();
        for (String file : saveFiles) {
            if (file.equals(QUICKSAVE_NAME)) {
                return GameStateSerializer.loadGameState(QUICKSAVE_NAME);
            }
        }
        return null;
    }
    
    /**
     * 自动保存游戏状态
     * 自动管理保存文件数量，删除过期的自动保存
     * @param gameContainer 游戏容器
     * @throws IOException 保存失败时抛出
     */
    public static void autoSave(GameContainer gameContainer) throws IOException {
        // 生成自动保存文件名
        String filename = GameStateSerializer.generateSaveFileName();
        GameStateSerializer.saveGameState(gameContainer, filename);
        
        // 清理过期的自动保存文件
        cleanupOldAutosaves();
    }
    
    /**
     * 保存游戏状态到指定文件
     * @param gameContainer 游戏容器
     * @param filename 文件名
     * @param description 保存描述
     * @throws IOException 保存失败时抛出
     */
    public static void saveWithDescription(GameContainer gameContainer, String filename, String description) throws IOException {
        // 保存游戏状态
        GameStateSerializer.saveGameState(gameContainer, filename);
        
        // 如果需要，可以单独保存描述信息到元数据文件
        // 这里暂时跳过，因为描述已经包含在GameSaveData中
    }
    
    /**
     * 获取所有保存文件的详细信息
     * @return 保存文件信息列表
     */
    public static List<SaveFileInfo> getSaveFileInfoList() {
        String[] saveFiles = GameStateSerializer.getSaveFileList();
        List<SaveFileInfo> infoList = new ArrayList<>();
          for (String filename : saveFiles) {
            try {
                GameSaveData saveData = GameStateSerializer.loadGameState(filename);
                if (saveData != null) {
                    SaveFileInfo info = new SaveFileInfo();
                    info.filename = filename;
                    info.saveTime = saveData.saveTime;
                    info.version = saveData.version;
                    info.description = saveData.description;
                    info.player1Name = saveData.player1Name;
                    info.player2Name = saveData.player2Name;
                    info.currentGameOrder = saveData.currentGameOrder;
                    info.isValid = saveData.isValid();
                    
                    infoList.add(info);
                } else {
                    // 如果saveData为null，创建一个错误的文件信息
                    SaveFileInfo info = new SaveFileInfo();
                    info.filename = filename;
                    info.isValid = false;
                    info.description = "文件内容为空或无法解析";
                    infoList.add(info);
                }
            } catch (IOException e) {
                // 如果读取失败，创建一个错误的文件信息
                SaveFileInfo info = new SaveFileInfo();
                info.filename = filename;
                info.isValid = false;
                info.description = "文件损坏或格式不兼容";
                infoList.add(info);
            } catch (Exception e) {
                // 捕获其他异常，如JSON解析异常
                SaveFileInfo info = new SaveFileInfo();
                info.filename = filename;
                info.isValid = false;
                info.description = "文件格式错误: " + e.getMessage();
                infoList.add(info);
            }
        }
        
        // 按保存时间排序（最新的在前）
        infoList.sort((a, b) -> {
            if (a.saveTime == null && b.saveTime == null) return 0;
            if (a.saveTime == null) return 1;
            if (b.saveTime == null) return -1;
            return b.saveTime.compareTo(a.saveTime);
        });
        
        return infoList;
    }
      /**
     * 恢复游戏状态到GameContainer
     * @param gameContainer 目标游戏容器
     * @param saveData 保存数据
     * @throws IllegalStateException 如果恢复失败
     */    public static void restoreGameState(GameContainer gameContainer, GameSaveData saveData) {
        if (!saveData.isValid()) {
            throw new IllegalStateException("保存数据无效，无法恢复游戏状态");
        }
        
        try {
            // 恢复游戏列表
            GameContainer.setGameList(saveData.gameList);
            
            // 打印当前游戏序号，用于调试
            System.out.println("从保存文件恢复游戏序号: " + saveData.currentGameOrder);
            
            // 恢复当前游戏序号 - 这一步必须在GameContainer构造函数调用之后执行
            // 之前的问题是GameContainer构造函数会重置currentGameOrder
            GameContainer.setCurrentGameOrder(saveData.currentGameOrder);
            
            // 确认序号已正确设置
            System.out.println("恢复后的当前游戏序号: " + GameContainer.getCurrentGameOrder());
            
            // 通知观察者游戏状态已更新
            // 在GameContainer中已通过setCurrentGameOrder通知UI
            
        } catch (Exception e) {
            throw new IllegalStateException("恢复游戏状态时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查是否存在快速保存
     * @return 如果存在快速保存返回true
     */
    public static boolean hasQuickSave() {
        String[] saveFiles = GameStateSerializer.getSaveFileList();
        for (String file : saveFiles) {
            if (file.equals(QUICKSAVE_NAME)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 删除指定的保存文件
     * @param filename 文件名
     * @return 如果删除成功返回true
     */
    public static boolean deleteSaveFile(String filename) {
        try {
            java.io.File file = new java.io.File("saves/" + filename + ".gamesave");
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 清理过期的自动保存文件
     */
    private static void cleanupOldAutosaves() {
        String[] saveFiles = GameStateSerializer.getSaveFileList();
        List<String> autosaveFiles = new ArrayList<>();
        
        // 收集所有自动保存文件
        for (String file : saveFiles) {
            if (file.startsWith(AUTOSAVE_PREFIX)) {
                autosaveFiles.add(file);
            }
        }
        
        // 如果自动保存文件数量超过限制，删除最旧的
        if (autosaveFiles.size() > MAX_AUTOSAVE_COUNT) {
            autosaveFiles.sort(String::compareTo); // 按文件名排序（时间戳）
            int filesToDelete = autosaveFiles.size() - MAX_AUTOSAVE_COUNT;
            
            for (int i = 0; i < filesToDelete; i++) {
                deleteSaveFile(autosaveFiles.get(i));
            }
        }
    }
    
    /**
     * 保存文件信息类
     */
    public static class SaveFileInfo {
        public String filename;
        public java.time.LocalDateTime saveTime;
        public String version;
        public String description;
        public String player1Name;
        public String player2Name;
        public int currentGameOrder;
        public boolean isValid;
        
        @Override
        public String toString() {
            if (!isValid) {
                return filename + " (无效文件)";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append(filename);
            if (saveTime != null) {
                sb.append(" - ").append(saveTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (player1Name != null && player2Name != null) {
                sb.append(" (").append(player1Name).append(" vs ").append(player2Name).append(")");
            }
            return sb.toString();
        }
    }
    
    /**
     * 启动时检查并询问是否加载存档
     * @return 如果选择加载存档则返回GameSaveData，否则返回null
     */
    public static GameSaveData checkAndLoadOnStartup(boolean isGuiMode) {
        List<SaveFileInfo> saveFiles = getSaveFileInfoList();
        
        // 过滤有效的存档文件
        List<SaveFileInfo> validSaves = saveFiles.stream()
                .filter(info -> info.isValid)
                .toList();
        
        if (validSaves.isEmpty()) {
            return null; // 没有有效存档
        }
        
        if (isGuiMode) {
            return showLoadGameDialogGUI(validSaves);
        } else {
            return showLoadGameDialogCLI(validSaves);
        }
    }
      /**
     * 退出时询问是否保存游戏
     * @param gameContainer 当前游戏容器
     * @param isGuiMode 是否为GUI模式
     * @return 是否成功保存或用户选择不保存
     */
    public static boolean checkAndSaveOnExit(GameContainer gameContainer, boolean isGuiMode) {
        if (isGuiMode) {
            // 使用Platform.runLater确保在JavaFX线程上执行UI操作
            final boolean[] result = {false};
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            
            Platform.runLater(() -> {
                try {
                    result[0] = showSaveGameDialogGUI(gameContainer);
                } finally {
                    latch.countDown();
                }
            });
            
            try {
                latch.await(); // 等待JavaFX线程完成UI操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            
            return result[0];
        } else {
            return showSaveGameDialogCLI(gameContainer);
        }
    }
    
    /**
     * GUI模式下显示加载游戏对话框
     */
    private static GameSaveData showLoadGameDialogGUI(List<SaveFileInfo> validSaves) {
        Alert loadAlert = new Alert(Alert.AlertType.CONFIRMATION);
        loadAlert.setTitle("加载游戏存档");
        loadAlert.setHeaderText("发现已有游戏存档");
        loadAlert.setContentText("是否要加载已有的游戏存档？");
        
        ButtonType loadButton = new ButtonType("加载存档");
        ButtonType newGameButton = new ButtonType("开始新游戏");
        ButtonType deleteButton = new ButtonType("删除存档并开始新游戏");
        ButtonType cancelButton = new ButtonType("取消", ButtonType.CANCEL.getButtonData());
        
        loadAlert.getButtonTypes().setAll(loadButton, newGameButton, deleteButton, cancelButton);
        
        Optional<ButtonType> result = loadAlert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == loadButton) {
                // 显示存档选择对话框
                ChoiceDialog<SaveFileInfo> saveChoice = new ChoiceDialog<>(validSaves.get(0), validSaves);
                saveChoice.setTitle("选择存档");
                saveChoice.setHeaderText("选择要加载的存档");
                saveChoice.setContentText("存档:");
                
                Optional<SaveFileInfo> saveResult = saveChoice.showAndWait();
                if (saveResult.isPresent()) {
                    try {
                        return GameStateSerializer.loadGameState(saveResult.get().filename);
                    } catch (IOException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("加载失败");
                        errorAlert.setHeaderText("无法加载存档");
                        errorAlert.setContentText("加载存档时出错: " + e.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            } else if (result.get() == deleteButton) {
                // 删除所有存档
                deleteAllSaveFiles();
                Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
                deleteAlert.setTitle("存档已删除");
                deleteAlert.setHeaderText(null);
                deleteAlert.setContentText("所有存档已删除，将开始新游戏。");
                deleteAlert.showAndWait();
            }
            // newGameButton 或其他情况都返回null，开始新游戏
        }
        
        return null;
    }    /**
     * CLI模式下显示加载游戏对话框
     */
    private static GameSaveData showLoadGameDialogCLI(List<SaveFileInfo> validSaves) {
        Scanner scanner = ScannerTools.getScanner();
        System.out.println("发现已有游戏存档：");
        System.out.println("1. 加载存档");
        System.out.println("2. 开始新游戏");
        System.out.println("3. 删除存档并开始新游戏");
        System.out.print("请选择 (1-3): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消费换行符
        
        switch (choice) {
            case 1:
                // 显示存档列表
                System.out.println("可用存档：");
                for (int i = 0; i < validSaves.size(); i++) {
                    System.out.println((i + 1) + ". " + validSaves.get(i));
                }
                System.out.print("请选择存档 (1-" + validSaves.size() + "): ");
                
                int saveChoice = scanner.nextInt();
                scanner.nextLine();
                
                if (saveChoice >= 1 && saveChoice <= validSaves.size()) {
                    try {
                        return GameStateSerializer.loadGameState(validSaves.get(saveChoice - 1).filename);
                    } catch (IOException e) {
                        System.err.println("加载存档失败: " + e.getMessage());
                    }
                } else {
                    System.out.println("无效选择，将开始新游戏。");
                }
                break;
                
            case 3:
                // 删除所有存档
                deleteAllSaveFiles();
                System.out.println("所有存档已删除，将开始新游戏。");
                break;
                
            default:
                System.out.println("将开始新游戏。");
                break;
        }
        
        return null;
    }
    
    /**
     * GUI模式下显示保存游戏对话框
     */
    private static boolean showSaveGameDialogGUI(GameContainer gameContainer) {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("保存游戏");
        saveAlert.setHeaderText("退出游戏");
        saveAlert.setContentText("是否要保存当前游戏进度？");
        
        ButtonType saveButton = new ButtonType("保存并退出");
        ButtonType exitButton = new ButtonType("直接退出");
        ButtonType cancelButton = new ButtonType("取消", ButtonType.CANCEL.getButtonData());
        
        saveAlert.getButtonTypes().setAll(saveButton, exitButton, cancelButton);
        
        Optional<ButtonType> result = saveAlert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == saveButton) {
                // 询问保存文件名
                TextInputDialog saveNameDialog = new TextInputDialog("game_" + System.currentTimeMillis());
                saveNameDialog.setTitle("保存游戏");
                saveNameDialog.setHeaderText("输入存档名称");
                saveNameDialog.setContentText("存档名称:");
                
                Optional<String> nameResult = saveNameDialog.showAndWait();
                if (nameResult.isPresent() && !nameResult.get().trim().isEmpty()) {
                    try {
                        GameStateSerializer.saveGameState(gameContainer, nameResult.get().trim());
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("保存成功");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("游戏已成功保存！");
                        successAlert.showAndWait();
                        return true;
                    } catch (IOException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("保存失败");
                        errorAlert.setHeaderText("无法保存游戏");
                        errorAlert.setContentText("保存游戏时出错: " + e.getMessage());
                        errorAlert.showAndWait();
                        return false;
                    }
                }
                return false; // 用户取消了保存文件名输入
            } else if (result.get() == exitButton) {
                return true; // 用户选择直接退出
            } else {
                return false; // 用户取消退出
            }
        }
        
        return false; // 默认取消
    }
      /**
     * CLI模式下显示保存游戏对话框
     */
    private static boolean showSaveGameDialogCLI(GameContainer gameContainer) {
        Scanner scanner = ScannerTools.getScanner();
        
        System.out.println("是否要保存当前游戏进度？");
        System.out.println("1. 保存并退出");
        System.out.println("2. 直接退出");
        System.out.println("3. 取消退出");
        System.out.print("请选择 (1-3): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符
            
            switch (choice) {
                case 1:
                    System.out.print("请输入存档名称: ");
                    String saveName = scanner.nextLine().trim();
                    if (!saveName.isEmpty()) {
                        try {
                            GameStateSerializer.saveGameState(gameContainer, saveName);
                            System.out.println("游戏已成功保存！");
                            return true;
                        } catch (IOException e) {
                            System.err.println("保存游戏失败: " + e.getMessage());
                            return false;
                        }
                    } else {
                        System.out.println("存档名称不能为空，退出已取消。");
                        return false;
                    }
                    
                case 2:
                    return true; // 直接退出
                    
                case 3:
                default:
                    return false; // 取消退出
            }
        } catch (Exception e) {
            System.out.println("输入无效，退出已取消。");
            return false;
        }
    }
      /**
     * 删除所有存档文件
     */
    private static void deleteAllSaveFiles() {
        String[] saveFiles = GameStateSerializer.getSaveFileList();
        for (String filename : saveFiles) {
            deleteSaveFile(filename);
        }
    }
}
