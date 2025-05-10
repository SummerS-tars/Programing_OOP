package top.thesumst.tools.input;

import java.awt.Point;

/**
 * 表示输入解析结果的类
 */
public class InputResult 
{
    private final InputType type;
    private final Object data;
    
    // 构造函数
    private InputResult(InputType type, Object data) 
    {
        this.type = type;
        this.data = data;
    }
    
    // 工厂方法
    public static InputResult chessMove(Point position) 
    {
        return new InputResult(InputType.CHESS_MOVE, position);
    }
    
    public static InputResult switchBoard(int boardNum) 
    {
        return new InputResult(InputType.SWITCH_BOARD, boardNum);
    }
    
    public static InputResult newGame(String gameType) 
    {
        return new InputResult(InputType.NEW_GAME, gameType);
    }
    
    public static InputResult pass() 
    {
        return new InputResult(InputType.PASS, null);
    }
    
    public static InputResult quit() 
    {
        return new InputResult(InputType.QUIT, null);
    }
    
    public static InputResult invalid() 
    {
        return new InputResult(InputType.INVALID, null);
    }

    public static InputResult useBomb(Point position) 
    {
        return new InputResult(InputType.USE_BOMB, position);
    }
    
    public static InputResult playback(String filename)
    {
        return new InputResult(InputType.PLAYBACK, filename);
    }
    
    // Getter方法
    public InputType getType() 
    {
        return type;
    }
    
    // 类型安全的获取方法
    @SuppressWarnings("unchecked")
    public <T> T getDataAs(Class<T> clazz) 
    {
        return (T) data;
    }
    
    // TODO: 这里不是类型安全的获取方法，应该需要重构一下
    public Point getPosition() 
    {
        return (Point) data;
    }
    
    public int getBoardNumber() 
    {
        return (Integer) data;
    }
    
    public String getGameType() 
    {
        return (String) data;
    }
    
    @Override
    public String toString() 
    {
        switch (type) 
        {
            case CHESS_MOVE:
                // TODO: 没有使用类型安全的获取方法，下面同
                Point p = getPosition();
                return "下棋: (" + (char)(p.y + 'A') + (p.x + 1) + ")";
            case SWITCH_BOARD:
                return "切换棋盘: " + getBoardNumber();
            case NEW_GAME:
                return "新建游戏: " + getGameType();
            case PASS:
                return "跳过回合";
            case QUIT:
                return "退出游戏";
            case USE_BOMB:
                Point bombPos = getPosition();
                return "使用炸弹: (" + (char)(bombPos.y + 'A') + (bombPos.x + 1) + ")";
            case PLAYBACK:
                // ? TODO: String.class的 class 是用来做什么的？
                String filename = getDataAs(String.class);
                return "播放DEMO: " + filename;
            default:
                return "无效输入";
        }
    }
}
