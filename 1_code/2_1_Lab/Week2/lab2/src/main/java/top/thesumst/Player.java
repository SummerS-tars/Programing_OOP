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

    public void initPlayer()
    {
        chessNumber = 2 ;   // * 注意一开始每个玩家都有两个棋子
        setName();
        setColor();
    }

    public void setName()
    {
        PrintTools.clearConsole();
        System.out.println("请输入你的玩家名称：");
        name = sc.nextLine() ;
    }

    public String getName()
    {
        return name ;
    }

    public void setColor()
    {
        boolean optionAvailable = false ;
        while( !optionAvailable )
        {
            PrintTools.clearConsole();
            System.out.println("请选择你的棋子颜色：(1.Black ○ | 2.White ● )");
            int op = sc.nextInt() ;
            sc.nextLine() ; // * 读取缓冲区中多余的换行符
            switch (op)
            {
                case 1:
                    color = ChessColor.BLACK ;
                    optionAvailable = true ;
                    break;
                case 2:
                    color = ChessColor.WHITE ;
                    optionAvailable = true ;
                    break;
                default:
                    System.out.println("无效的选择！请键入任何按键以重新选择") ;
                    sc.nextLine() ;
                    break;
            }
        }
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
