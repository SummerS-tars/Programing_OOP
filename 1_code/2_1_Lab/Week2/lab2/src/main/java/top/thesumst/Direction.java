package top.thesumst;

import java.awt.Point;
import java.util.EnumSet;

/**
 * Direction枚举定义了八个方向和一个字节中为1的位的对应关系
 * 给出了获得方向对应值和对应单位坐标变化的方法
 */
public enum Direction 
{
    LEFT_UP(1),
    UP(1<<1),
    RIGHT_UP(1<<2),
    RIGHT(1<<3),
    RIGHT_DOWN(1<<4),
    DOWN(1<<5),
    LEFT_DOWN(1<<6),
    LEFT(1<<7);

    private final byte value ;

    Direction(int number)
    {
        this.value = (byte)number ;
    }

    public byte getValue()
    {
        return value ;
    }

    public static Point getDirectionDelta(Direction dir)
    {
        switch(dir)
        {
            case LEFT_UP:
                return DirectionDelta.LEFT_UP.getDirectionDelta();
            case UP:
                return DirectionDelta.UP.getDirectionDelta();
            case RIGHT_UP:
                return DirectionDelta.RIGHT_UP.getDirectionDelta();
            case RIGHT:
                return DirectionDelta.RIGHT.getDirectionDelta();
            case RIGHT_DOWN:
                return DirectionDelta.RIGHT_DOWN.getDirectionDelta();
            case DOWN:
                return DirectionDelta.DOWN.getDirectionDelta();
            case LEFT_DOWN:
                return DirectionDelta.LEFT_DOWN.getDirectionDelta();
            case LEFT:
                return DirectionDelta.LEFT.getDirectionDelta();
            default:
                throw new IllegalArgumentException("Invalid direction: " + dir);
        }
    }
}


/**
 * DirectionDelta枚举定义了八个方向和对应单位坐标变化的对应关系
 */
enum DirectionDelta
{
    LEFT_UP(-1,-1),
    UP(-1,0),
    RIGHT_UP(-1,1),
    RIGHT(0,1),
    RIGHT_DOWN(1,1),
    DOWN(1,0),
    LEFT_DOWN(1,-1),
    LEFT(0,-1);

    private final Point delta ;

    DirectionDelta(int row , int col)
    {
        this.delta = new Point(row, col) ;
    }

    public Point getDirectionDelta()
    {
        return delta ;
    }
}

/**
 * Direction EnumSet测试
 */
class DirectionTestDrive
{
    public static void main(String[] args)
    {
        // 创建包含所有Direction的EnumSet
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);

        // 遍历所有方向并实现输出
        for(Direction direction : allDirections)
        {
            System.out.println("direction : " + direction) ;
            System.out.println("delta of direction : " + direction.getDirectionDelta(direction) );
        }
    }
}