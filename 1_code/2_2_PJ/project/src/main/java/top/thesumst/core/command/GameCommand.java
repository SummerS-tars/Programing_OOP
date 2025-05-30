package top.thesumst.core.command;

import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;

/**
 * 游戏命令接口
 */
public interface GameCommand 
{
    /**
     * 执行命令
     * @param game 当前游戏
     * @param gameList 游戏列表
     * @return 执行结果
     */
    CommandResult execute();
}