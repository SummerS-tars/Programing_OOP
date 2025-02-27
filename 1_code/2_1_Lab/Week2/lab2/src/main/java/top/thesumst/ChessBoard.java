package top.thesumst;

import java.util.*;

/**
 * class ChessBoard store the chess board data
 */
public class ChessBoard 
{

    // // * set chess color char
    // public static final char WHITE_CHESS = '●' ;
    // public static final char BLACK_CHESS = '○' ;
    // public static final char BLANK = '·' ;

    private ChessColor[][] chessBoard ;
    private boolean blackTurn ; // * true表示黑色下棋，false表示白色下棋
    private Player p1 , p2 ;
    private Scanner sc = new Scanner(System.in) ;

    public void initChessBoard()
    {
        chessBoard = new ChessColor[8][8] ;
        for (int i = 0; i < chessBoard.length; i++) 
            Arrays.fill(chessBoard[i], ChessColor.BLANK); // * 填充每一行
        chessBoard[3][3] = chessBoard[4][4] = ChessColor.WHITE ;
        chessBoard[3][4] = chessBoard[4][3] = ChessColor.BLACK ;

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

    public void setChessColor(int row , int col , ChessColor color)
    {
        chessBoard[row][col] = color ;
    }

    public ChessColor getChessColor(int row , int col)
    {
        return chessBoard[row][col] ;
    }

    public void setBlackTurn(boolean isTurn)
    {
        blackTurn = isTurn ;
    }

    public boolean getBlackTurn()
    {
        return blackTurn ;
    }

    public Player getPlayer(ChessColor color)
    {
        return color == p1.getColor() ? p1 : p2 ;
    }

    public boolean checkGo(int row , int col)
    {
        // TODO: 实现棋步检测
    }

    public void go(int row , int col)
    {
        setChessColor(row, col, blackTurn? ChessColor.BLACK : ChessColor.WHITE);
    }

    public void reverseChess()
    {
        // TODO: 实现翻转（大致思路，可能需要靠递归判断）
    }

    public boolean checkTurn()
    {
        // TODO: 实现检查是否换方和是否游戏结束
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
        System.out.println("WHITE_CHESS is " + ChessColor.WHITE.getSymbol() ) ;
        System.out.println("BLACK_CHESS is " + ChessColor.BLACK.getSymbol() ) ;
        System.out.println("BLANK is " + ChessColor.BLANK.getSymbol() ) ;
        System.out.printf("\nPrint the Initialized ChessBoard\n") ;
        for(int i = 0 ; i < 8 ; i ++ )
        {
            for( int j = 0 ; j < 8 ; j ++ )
                System.out.printf("%c " , cb.getChessColor(i, j).getSymbol()) ;
            System.out.println() ;
        }
        System.out.println("After Init of ChessBoard") ;
        System.out.printf("Black player: %s\nWhite player: %s\n", 
            cb.getPlayer(ChessColor.BLACK).getName(), 
            cb.getPlayer(ChessColor.WHITE).getName());
        System.out.printf("Black player initial chess count: %d\n", 
            cb.getPlayer(ChessColor.BLACK).getChessNumber());
        System.out.printf("White player initial chess count: %d\n", 
            cb.getPlayer(ChessColor.WHITE).getChessNumber());
    }
}
