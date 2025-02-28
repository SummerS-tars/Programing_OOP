package top.thesumst;

import java.util.*;
import java.awt.Point;

/**
 * class ChessBoard store the chess board data
 */
public class ChessBoard 
{
    //// deprecated and replaced by enum ChessColor
    //// public static final char WHITE_CHESS = '●' ;
    //// public static final char BLACK_CHESS = '○' ;
    //// public static final char BLANK = '·' ;

    private ChessColor[][] chessBoard ;
    private boolean blackTurn ; // * true表示黑色下棋，false表示白色下棋
    private Player p1 , p2 ;
    private Scanner sc = new Scanner(System.in) ;

    /**
     * * initChessBoard方法用于初始化棋盘以及玩家数据
     */
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

    /**
     * * setChessColor方法，设置point位置棋盘棋子
     * @param point
     * @param color
     */
    public void setChessColor(Point point , ChessColor color)
    {
        chessBoard[point.x][point.y] = color ;
    }

    /**
     * * getChessColor方法，获取point位置棋盘棋子颜色
     * @param point
     * @return ChessColor
     */
    public ChessColor getChessColor(Point point)
    {
        return chessBoard[point.x][point.y] ;
    }

    // 基本被弃用的接口，因为blackTurn基本只在本类的实例中使用
    public void setBlackTurn(boolean isTurn)
    {
        blackTurn = isTurn ;
    }

    public boolean getBlackTurn()
    {
        return blackTurn ;
    }

    /**
     * * 返回某种指定颜色棋子的玩家
     * @param color
     * @return Player
     */
    public Player getPlayer(ChessColor color)
    {
        return color == p1.getColor() ? p1 : p2 ;
    }

    /**
     * * checkGo方法实现对玩家输入的棋步合法性检验
     * @param point
     * @return byte
     * 合法方向信息串
     */
    public byte checkGo(Point point)
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
        byte legalDirection = 0 ;


        // TODO: 测试！！！
        // 调试步骤
        System.out.println("point:" + point);



        // * 当前位置已经有棋子，非法步骤
        if(getChessColor(point) != ChessColor.BLANK) return legalDirection ;

        // * 遍历八个方向寻找是否有合法方向
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            // TODO: test
            System.out.println("direction:" + direction);


            Point focus = new Point(point) ;
            boolean endFlag = false ;
            while(endFlag = moveFocus(focus, direction))
            {
                // TODO: test
                System.out.println("focus:" + focus);


                // ! fix try 1.1 修复合法棋步检测问题 但似乎返回还是有问题 
                // * 判断是否异色
                if(getChessColor(focus) == (blackTurn ? ChessColor.WHITE : ChessColor.BLACK))
                {
                    // * 是异色棋子，维持该方向位数字为1
                    legalDirection |= direction.getValue() ;
                }
                else
                {
                    break ;
                    /**
                     * 注意：此处包含多种情况
                     * 1. 该方向为合法方向，保持方向位为1进入下一个方向
                     * 2. 该方向相邻为空或者为同色棋子，保持方向位为0进入下一个方向
                     * 3. 该方向有相邻异色棋子，但没有同色棋子包夹，保持方向位为0进入下一个方向
                     */
                }
            }
            if(!endFlag) legalDirection &= ~(direction.getValue()) ; // * 处理走到底或者出界的情况，确保该方向位为0

            // TODO: test
            System.out.println(Integer.toBinaryString(legalDirection));
        }
        return legalDirection ;
    }

    /**
     * * go方法，用于实施下棋和翻转棋子的步骤
     * @param point
     * @param legalDirection
     */
    public void go(Point point , byte legalDirection)
    {
        // TODO: 实现一步棋+翻转棋子
        // * 本次运行要放置的棋子颜色设置
        ChessColor color = blackTurn ? ChessColor.BLACK : ChessColor.WHITE ;

        // * 棋子翻转
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            Point focus = new Point(point) ;

            // * 判断是否为有效方向
            if((direction.getValue() & legalDirection) == 0 ) continue ;
            else
            {
                // * 该方向为有效方向，开始翻转棋子
                while(getChessColor(focus) != color)
                {
                    setChessColor(focus, color);
                    moveFocus(focus, direction);    // * 此处抛弃了返回值，因为有效方向一定会有同色棋子包夹
                }
            }
        }
        
        // * 原位置补上棋子
        setChessColor(point, color) ;
    }

    /**
     * * checkTurn方法，检测是否需要换边或游戏是否结束
     * @return boolean
     * true 游戏继续
     * false 游戏结束
     */
    public boolean checkTurn()
    {
        // TODO: 实现检查是否换方和是否游戏结束
        
        // * 先检测另一方是否有合法位置
        blackTurn = !blackTurn ;
        if(checkSide()) return true ; // * 检测到另一方有合法位置，换边并进入下一轮
        
        // * 检测已下棋方是否有合法位置
        blackTurn = !blackTurn ;
        if(checkSide()) return true ;

        // * 双方均无合法位置，退出游戏
        return false ;
    }
    
    /**
     * * checkSide，用于确认当前blackTurn下的Player是否具有合法位置
     * @return boolean
     * true 有
     * false 没有
     */
    private boolean checkSide()
    {
        for(int i = 0 ; i < 8 ; i ++ )
        {
            for(int j = 0 ; j < 8 ; j ++ )
            {
                Point focus = new Point(i , j) ;
                if(getChessColor(focus) == ChessColor.BLANK)
                {
                    if(checkGo(focus) != 0) return true ; // * 存在合法方向
                }
            }
        }
        return false ;
    }

    /**
     * * updatePlayerChessNumber方法，更新玩家棋子数量
     */
    public void updatePlayerChessNumber()
    {
        int blackChessNumber = 0 ;
        int whiteChessNumber = 0 ;
        for(int i = 0 ; i < 8 ; i ++ )
        {
            for(int j = 0 ; j < 8 ; j ++ )
            {
                if(chessBoard[i][j] == ChessColor.BLACK) blackChessNumber ++ ;
                else if(chessBoard[i][j] == ChessColor.WHITE) whiteChessNumber ++ ;
            }
        }
        p1.setChessNumber(blackChessNumber);
        p2.setChessNumber(whiteChessNumber);
    }

    /**
     * * moveFocus移动检查焦点
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

    /**
     * * 打印棋盘及对局信息
     */
    public void printChessBoard()
    {
        // TODO: 棋盘信息打印完善
        PrintTools.clearConsole();
        System.out.println("  1 2 3 4 5 6 7 8") ;
        for(int i = 0 ; i < 8 ; i ++ )
        {
            System.out.printf("%c " , (char)('A' + i)) ;
            for(int j = 0 ; j < 8 ; j ++ )
            {
                System.out.printf("%c " , chessBoard[i][j].getSymbol()) ;
            }
            if(i == 4) System.out.printf("\t\t\t%8s %c : %d" ,
                        p1.getName() , p1.getColor().getSymbol() , p1.getChessNumber()) ;
            else if(i == 5) System.out.printf("\t\t\t%8s %c : %d" , 
                        p2.getName() , p2.getColor().getSymbol() , p2.getChessNumber()) ;
            System.out.println() ;
        }
    }

    /**
     * * getWinner方法，获取胜利方
     * @return Player
     */
    public Player getWinner()
    {
        return p1.getChessNumber() > p2.getChessNumber() ? p1 : p2 ;
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
                System.out.printf("%c " , cb.getChessColor(new Point(i, j)).getSymbol()) ;
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
