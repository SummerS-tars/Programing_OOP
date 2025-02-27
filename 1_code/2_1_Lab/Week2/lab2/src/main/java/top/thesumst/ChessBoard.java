package top.thesumst;

import java.util.*;
import java.awt.Point;

/**
 * class ChessBoard store the chess board data
 */
public class ChessBoard 
{
    // ! deprecated
    //// public static final char WHITE_CHESS = '●' ;
    //// public static final char BLACK_CHESS = '○' ;
    //// public static final char BLANK = '·' ;

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

    /**
     * *checkGo方法实现对玩家输入的棋步合法性检验
     * @param point
     * @return 
     */
    public boolean checkGo(Point point)
    {
        // TODO: 实现棋步检测
        /**
         * 检测条件构思：
         * 0. 不超出棋盘（或许在输入转换方法中实现）
         * 1. 周围八格棋子检验 & 记录有异色棋子方向
         *      next direction 无相邻异色棋子 + 记录0不变
         *      next direction 有相邻异色棋子 + 改记录1
         *      out 无相邻异色棋子
         *      go on 有相邻异色棋子
         * 2. 异色棋子方向检验 & 记录包夹方向
         *      next direction 无同色棋子夹住 + 改记录0
         *      next direction 有同色棋子夹住 + 记录1不变
         *      out 无包夹成功方向
         *      go on 有包夹成功方向
         */


        // if((blackTurn ? ChessColor.BLACK : ChessColor.WHITE) == )

        // * 当前位置已经有棋子，非法步骤
        if(chessBoard[point.x][point.y] != ChessColor.BLANK) return false ;

        
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        byte legalDirection = 0 ;
        for(Direction direction : allDirections )
        {
            Point focus = new Point(point) ;
            while(moveFocus(focus, direction))
            {
                // * 判断是否异色
                if(chessBoard[focus.x][focus.y] == 
                (blackTurn ? ChessColor.WHITE : ChessColor.BLACK)
                {
                    
                }
                else    // 
                {

                }
            }
        }
    }

    public void go(Point point)
    {
        // TODO: 实现一步棋+翻转（大致思路，可能需要靠递归判断）
    }

    public boolean checkTurn()
    {
        // TODO: 实现检查是否换方和是否游戏结束
    }

    /**
     * moveFocus移动检查焦点
     * @param focus
     * @param direction
     * @return boolean
     * true : successfully move
     * false : unable to move
     */
    private static boolean moveFocus(Point focus , Direction direction)
    {
        int dx = Direction.getDirectionDelta(direction).x ;
        int dy = Direction.getDirectionDelta(direction).y ;

        if( focus.x + dx >= 0 && focus.x + dx <= 8 &&
            focus.y + dy >= 0 && focus.y + dy <= 8)
        {
            focus.x += dx ;
            focus.y += dy ;
            return true ;
        }
        else return false ;
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
