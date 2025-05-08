package top.thesumst.mode.component;

import top.thesumst.type.ChessColor;
import java.awt.Point;

public class Step 
{
    private Point point ;
    private ChessColor color ;

    public Step(Point point, ChessColor color)
    {
        this.point = point ;
        this.color = color ;
    }

    public Point getPoint()
    {
        return point;
    }

    public ChessColor getColor()
    {
        return color;
    }
}
