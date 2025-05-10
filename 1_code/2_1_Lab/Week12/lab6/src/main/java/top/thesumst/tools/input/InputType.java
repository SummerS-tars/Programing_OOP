package top.thesumst.tools.input;

/**
 * 表示输入命令的类型
 */
public enum InputType 
{
    CHESS_MOVE,    // 下棋
    SWITCH_BOARD,  // 切换棋盘
    NEW_GAME,      // 新建游戏
    PASS,          // 跳过回合
    QUIT,          // 退出游戏
    INVALID,       // 无效输入
    USE_BOMB       // 使用炸弹
}
