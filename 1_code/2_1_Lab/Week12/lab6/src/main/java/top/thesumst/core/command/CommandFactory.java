package top.thesumst.core.command;

import top.thesumst.io.input.InputResult;

/**
 * 命令工厂类 - 根据输入结果创建相应的命令
 */
public class CommandFactory {
    /**
     * 创建对应的命令
     * @param result 输入解析结果
     * @return 命令对象，如果输入无效则返回null
     */
    public static GameCommand createCommand(InputResult result) 
    {
        if (result == null) return null;
        switch (result.getType()) 
        {
            case CHESS_MOVE:
                return new GoCommand(result.getPosition());
                
            case SWITCH_BOARD:
                return new SwitchBoardCommand(result.getBoardNumber());
                
            case PASS:
                return new PassCommand();
                
            case NEW_GAME:
                return new NewGameCommand(result.getGameType());
                
            case QUIT:
                return new QuitCommand();

            case USE_BOMB:
                return new BombCommand(result.getPosition());

            case INVALID:
            default:
                return null;
        }
    }
}