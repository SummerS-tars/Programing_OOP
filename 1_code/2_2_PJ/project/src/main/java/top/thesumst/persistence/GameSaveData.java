package top.thesumst.persistence;

import top.thesumst.core.container.GameList;
import java.time.LocalDateTime;

/**
 * 游戏保存数据结构
 * 包含完整的游戏状态信息
 */
public class GameSaveData {
    
    /**
     * 游戏列表，包含所有游戏模式的状态
     */
    public GameList gameList;
    
    /**
     * 当前活动的游戏序号
     */
    public int currentGameOrder;
    
    /**
     * 保存时间
     */
    public LocalDateTime saveTime;
    
    /**
     * 保存格式版本号
     */
    public String version;
    
    /**
     * 保存描述信息（可选）
     */
    public String description;
    
    /**
     * 玩家名称信息
     */
    public String player1Name;
    public String player2Name;
    
    /**
     * 构造函数
     */
    public GameSaveData() {
        this.saveTime = LocalDateTime.now();
        this.version = "1.0";
    }
    
    /**
     * 验证保存数据的完整性
     * @return 如果数据有效返回true，否则返回false
     */
    public boolean isValid() {
        return gameList != null && 
               currentGameOrder > 0 && 
               saveTime != null && 
               version != null;
    }
    
    /**
     * 获取保存数据的摘要信息
     * @return 保存数据摘要字符串
     */
    public String getSummary() {
        if (!isValid()) {
            return "Invalid save data";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("保存时间: ").append(saveTime.toString()).append("\n");
        summary.append("版本: ").append(version).append("\n");
        summary.append("当前游戏: ").append(currentGameOrder).append("\n");
        
        if (player1Name != null && player2Name != null) {
            summary.append("玩家: ").append(player1Name).append(" vs ").append(player2Name).append("\n");
        }
        
        if (description != null && !description.isEmpty()) {
            summary.append("描述: ").append(description);
        }
        
        return summary.toString();
    }
}
