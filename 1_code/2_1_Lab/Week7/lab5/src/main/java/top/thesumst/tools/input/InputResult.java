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
            default:
                return "无效输入";
        }
    }
}
