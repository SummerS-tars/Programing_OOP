package top.thesumst.mode.component;

import top.thesumst.type.ChessStatement;

public class Player
{
    private String name ;
    private ChessStatement color ;
    private int chessNumber ;

    public Player(String name, ChessStatement color)
    {
        this.name = name ;
        this.color = color ;
        chessNumber = 2 ;
    }

    public String getName()
    {
        return name ;
    }
        
    public ChessStatement getColor() 
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