package top.thesumst.mode.component;

import top.thesumst.type.ChessStatement;
import java.awt.Point;

public class Step 
{
    private Point point ;
    private ChessStatement color ;

    public Step(Point point, ChessStatement color)
    {
        this.point = point ;
        this.color = color ;
    }

    public Point getPoint()
    {
        return point;
    }

    public ChessStatement getColor()
    {
        return color;
    }
}
