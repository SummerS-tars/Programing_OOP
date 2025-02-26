package top.thesumst;

import java.util.*;

public class ReversiGame
{

}

/**
 * class ChessBoard store the chess board data
 */
class ChessBoard
{
    // * set chess color char
    public static final char WHITE_CHESS = '●' ;
    public static final char BLACK_CHESS = '○' ;
    public static final char BLANK = '·' ;

    private char[][] chessColor ;
    private boolean blackTurn ; // * true表示黑色下棋，false表示白色下棋
    private Player p1 , p2 ;
    private Scanner sc = new Scanner(System.in) ;

    public void initChessBoard()
    {
        chessColor = new char[8][8] ;
        for (int i = 0; i < chessColor.length; i++) 
            Arrays.fill(chessColor[i], ChessBoard.BLANK); // * 填充每一行
        chessColor[3][3] = chessColor[4][4] = ChessBoard.WHITE_CHESS ;
        chessColor[3][4] = chessColor[4][3] = ChessBoard.BLACK_CHESS ;

        p1 = new Player() ;
        p1.initPlayer();
        p2 = new Player() ;
        p2.initPlayer();
        while(p2.getColor() == p1.getColor())   // * 排除两人选择一样颜色的情况
        {
            System.out.println("两人不可以选择同一种颜色的棋子！请键入任何按键以重新选择") ;
            sc.nextLine() ;
            p2.setColor();
        }

        blackTurn = true ;
    }

    public void setChessColor(int x , int y , char color)
    {
        chessColor[x][y] = color ;
    }

    public char getChessColor(int x , int y)
    {
        return chessColor[x][y] ;
    }

    public void setBlackTurn(boolean isTurn)
    {
        blackTurn = isTurn ;
    }

    public boolean getBlackTurn()
    {
        return blackTurn ;
    }

    public Player getPlayer(char color)
    {
        return color == p1.getColor() ? p1 : p2 ;
    }
}

/**
 * class Player stores the player data
 * create a player and use init
 * use set and get operations to operate the data
 */
class Player
{
    private String name ;
    private char color ;
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
                    color = ChessBoard.BLACK_CHESS ;
                    optionAvailable = true ;
                    break;
                case 2:
                    color = ChessBoard.WHITE_CHESS ;
                    optionAvailable = true ;
                    break;
                default:
                    System.out.println("无效的选择！请键入任何按键以重新选择") ;
                    sc.nextLine() ;
                    break;
            }
        }
    }

    public char getColor() 
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

class PrintTools
{
    public static void clearConsole()
    {
        /**
         * * 转义字符说明：
         * * \033 ESC (ASCII 27)
         * * [H 光标位置命令，将光标移回屏幕左上角（行1，列1）
         * * [2J 清屏
         * 
         * * System.out.flush() 用于确保输出生效
         */
        System.out.print("\033[H\033[2J") ;
        System.out.flush();
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
        System.out.println("player chess is " + player.getColor());
    }
}

/**
 * Initial Test for class ChessBoard initChessBoard operation
 */
class InitialChessBoardTestDrive
{
    public static void main(String[] args)
    {
        ChessBoard cb = new ChessBoard() ;
        cb.initChessBoard();
        System.out.printf("About ChessBoard :\nIt's black turn : %b\n", cb.getBlackTurn()) ;
        System.out.println("WHITE_CHESS is " + ChessBoard.WHITE_CHESS ) ;
        System.out.println("BLACK_CHESS is " + ChessBoard.BLACK_CHESS ) ;
        System.out.println("BLANK is " + ChessBoard.BLANK ) ;
        System.out.printf("\nPrint the Initialized ChessBoard\n") ;
        for(int i = 0 ; i < 8 ; i ++ )
        {
            for( int j = 0 ; j < 8 ; j ++ )
                System.out.printf("%c " , cb.getChessColor(i, j)) ;
            System.out.println() ;
        }
        System.out.println("After Init of ChessBoard") ;
        System.out.printf("Black player: %s\nWhite player: %s\n", 
            cb.getPlayer(ChessBoard.BLACK_CHESS).getName(), 
            cb.getPlayer(ChessBoard.WHITE_CHESS).getName());
        System.out.printf("Black player initial chess count: %d\n", 
            cb.getPlayer(ChessBoard.BLACK_CHESS).getChessNumber());
        System.out.printf("White player initial chess count: %d\n", 
            cb.getPlayer(ChessBoard.WHITE_CHESS).getChessNumber());
    }
}
