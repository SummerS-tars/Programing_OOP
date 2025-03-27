package top.thesumst.mode.component;

import top.thesumst.type.ChessColor;

public class Player
{
    private String name ;
    private ChessColor color ;
    private int chessNumber ;

    public Player(String name, ChessColor color)
    {
        this.name = name ;
        this.color = color ;
        chessNumber = 2 ;
    }

    public String getName()
    {
        return name ;
    }
        
    public ChessColor getColor() 
    {
        return color;
    }

    public void setChessNumber(int num)
    {
        chessNumber = num ;
    }

    public int getChessNumber()
    {
        return chessNumber ;
    }
}