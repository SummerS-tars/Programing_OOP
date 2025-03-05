package top.thesumst;

import java.util.*;

/**
 * class Player stores the player data
 * create a player and use init
 * use set and get operations to operate the data
 */
public class Player
{
    private String name ;
    private ChessColor color ;
    private int chessNumber ;
    private Scanner sc = new Scanner(System.in) ; // ? 为什么放在方法内部新建Scanner总是会在读取完name后读color时报错

    Player(String name, ChessColor color)
    {
        this.name = name ;
        this.color = color ;
        chessNumber = 2 ;   // * 注意一开始每个玩家都有两个棋子
    }

    // deprecated
    // public void setName(String name)
    // {
    //     this.name = name ;
    // }

    
    // public void setColor(ChessColor color)
    // {
    //     this.color = color ;
    // }

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

/**
 * Initial Test for class Player
 */
class PlayerInitTestDrive
{
    public static void main(String[] args)
    {
        Player player = new Player() ;
        player.initPlayer();
        PrintTools.clearConsole();
        System.out.println("player name is " + player.getName());
        System.out.println("player chess is " + player.getColor().getSymbol());
    }
}
