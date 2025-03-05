package top.thesumst;

import java.util.*;
import java.awt.Point;
import java.io.IOException;
import java.util.logging.*;

/**
 * class ChessBoard store the chess board data
 */
public class ChessBoard 
{
    // : test logger for ChessBoard
    // private static final Logger logger = Logger.getLogger(ChessBoard.class.getName());
    // static {
    //     try {
    //         LogManager.getLogManager().reset();
    //         FileHandler fh = new FileHandler("chessboard.log", true);
    //         fh.setFormatter(new SimpleFormatter());
    //         logger.addHandler(fh);
    //         logger.setLevel(Level.ALL);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    //// deprecated and replaced by enum ChessColor
    //// public static final char WHITE_CHESS = '●' ;
    //// public static final char BLACK_CHESS = '○' ;
    //// public static final char BLANK = '·' ;

    private ChessColor[][] chessBoard ;
    private boolean blackTurn ; // * true表示黑色下棋，false表示白色下棋
    private Player p1 , p2 ;
    // * v1.0 feature add 4.1 增加玩家回合信息
    private static String blackTurnInfo ;
    private static String whiteTurnInfo ;
    private Scanner sc = new Scanner(System.in) ;
    private boolean gameEndFlag = false ;

    /**
     * * ChessBoard方法用于初始化棋盘以及玩家数据
     */
    ChessBoard(String player1Name, String player2Name, ChessColor player1ChessColor, ChessColor player2ChessColor)
    {
        chessBoard = new ChessColor[8][8] ;
        for (int i = 0; i < chessBoard.length; i++) 
            Arrays.fill(chessBoard[i], ChessColor.BLANK); // * 填充每一行
        chessBoard[3][3] = chessBoard[4][4] = ChessColor.WHITE ;
        chessBoard[3][4] = chessBoard[4][3] = ChessColor.BLACK ;

        p1 = new Player(player1Name, player1ChessColor) ;
        p2 = new Player(player1Name, player2ChessColor) ;

        blackTurn = true ;

        // * v1.0 feature add 4.2 增加玩家回合信息
        blackTurnInfo = "现在是" + (p1.getColor() == ChessColor.BLACK ? p1.getName() : p2.getName())  + ChessColor.BLACK.getSymbol() + " 下棋" ;
        whiteTurnInfo = "现在是" + (p1.getColor() == ChessColor.WHITE ? p1.getName() : p2.getName())  + ChessColor.WHITE.getSymbol() + " 下棋" ;
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

    public void setGameEndFlag(boolean flag)
    {
        gameEndFlag = flag ;
    }

    public boolean getGameEndFlag()
    {
        return gameEndFlag ;
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


        // test checkGo
        // logger.info("start check point:" + point);



        // * 当前位置已经有棋子，非法步骤
        if(getChessColor(point) != ChessColor.BLANK) return legalDirection ;

        // * 遍历八个方向寻找是否有合法方向
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            // test direction
            // logger.info("start check direction:" + direction);


            boolean legalFlag = false ; // ! fix try 3.1 修复运行逻辑问题，调整辅助判断参数
            /**
             * * v1.0 bug fix 中将endFlag改为legalFlag
             * * 从表示是否走到底 改为更合适的 表示合法方向的判断辅助参数
             * * 用于辅助判断是否有合法方向
             */

            Point focus = new Point(point) ;
            while(legalFlag = moveFocus(focus, direction))
            {
                // test focus move
                // logger.info("focus:" + focus);

 
                // * 判断是否异色
                if(getChessColor(focus) == (blackTurn ? ChessColor.WHITE : ChessColor.BLACK))   // ! fix try 1.1 修复合法棋步检测问题
                {
                    // * 是异色棋子，维持该方向位数字为1
                    legalDirection |= direction.getValue() ;
                }
                else
                {
                    // ! fix try 3.2 修复运行逻辑问题，增设空白判断机制，调整辅助判断参数
                    if(getChessColor(focus) == ChessColor.BLANK) legalFlag = false ; // * 该方向为空，将legalFlag置为false再退出
                    break ;
                    /**
                     * 注意：此处包含多种情况
                     * 1. 该方向相邻异色棋子且有同色棋子包夹，保持 legalFlag:true 方向位:1 进入下一个方向
                     * 2. 该方向直接为空或者相邻异色棋子之后为空，设置 legalFlag:false 经过方向位重置后 进入下一个方向
                     * 3. 该方向相邻同色棋子， 保持 legalFlag:true 方向位:0 进入下一个方向
                     */
                }
            }
            // ! fix try 3.3 修复运行逻辑问题，调整辅助判断参数
            if(!legalFlag) legalDirection &= ~(direction.getValue()) ; // * 处理走到底或者出界的情况，确保该方向位为0

            // test legalDirection
            // logger.info(Integer.toBinaryString(legalDirection));
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
        // test direction
        // logger.info("start check go !!!");


        // * 本次运行要放置的棋子颜色设置
        ChessColor color = blackTurn ? ChessColor.BLACK : ChessColor.WHITE ;

        // * 棋子翻转
        EnumSet<Direction> allDirections = EnumSet.allOf(Direction.class);
        for(Direction direction : allDirections )
        {
            
            // * 判断是否为有效方向
            if((direction.getValue() & legalDirection) == 0 ) continue ;
            else
            {
                // test direction reverse chesses
                // logger.info("start revers chesses in direction:" + direction);


                Point focus = new Point(point) ;
                moveFocus(focus, direction) ; // ! bug fix try 4.1 修复翻转棋子时只能翻转按照Direction顺序的第一个合法方向棋子的问题

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
        // test checkSide
        // logger.info("start checkSide !!!");

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
        // test moveFocus
        // logger.info("start moveFocus !!!");

        int dx = Direction.getDirectionDelta(direction).x ;
        int dy = Direction.getDirectionDelta(direction).y ;


        if( focus.x + dx >= 0 && focus.x + dx < 8 &&
            focus.y + dy >= 0 && focus.y + dy < 8)      // ! fix try 2.1 修复移动焦点的边界检测问题
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
        // TODO: 增加彩色打印功能
        PrintTools.clearConsole();
        System.out.println("  A B C D E F G H") ;
        for(int i = 0 ; i < 8 ; i ++ )
        {
            System.out.printf("%c " , (char)('1' + i)) ;
            for(int j = 0 ; j < 8 ; j ++ )
            {
                System.out.printf("%c " , chessBoard[i][j].getSymbol()) ;
            }
            // ! little fix try 1.1 修复玩家打印位置向下一偏移了行的错误
            if(i == 3) System.out.printf("\t\t\t%8s %c : %d" ,
                        p1.getName() , p1.getColor().getSymbol() , p1.getChessNumber()) ;
            else if(i == 4) System.out.printf("\t\t\t%8s %c : %d" , 
                        p2.getName() , p2.getColor().getSymbol() , p2.getChessNumber()) ;
            System.out.println() ;
        }

        // * v1.0 feature add 4.3 增加玩家回合信息打印
        System.out.println(blackTurn ? blackTurnInfo : whiteTurnInfo) ;
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

// deprecated
// /**
//  * Initial Test for class ChessBoard initChessBoard operation
//  */
// class InitialChessBoardTestDrive
// {
//     public static void main(String[] args)
//     {
//         ChessBoard cb = new ChessBoard() ;
//         cb.initChessBoard();
//         System.out.printf("About ChessBoard :\nIt's black turn : %b\n", cb.getBlackTurn()) ;
//         System.out.println("WHITE_CHESS is " + ChessColor.WHITE.getSymbol() ) ;
//         System.out.println("BLACK_CHESS is " + ChessColor.BLACK.getSymbol() ) ;
//         System.out.println("BLANK is " + ChessColor.BLANK.getSymbol() ) ;
//         System.out.printf("\nPrint the Initialized ChessBoard\n") ;
//         for(int i = 0 ; i < 8 ; i ++ )
//         {
//             for( int j = 0 ; j < 8 ; j ++ )
//                 System.out.printf("%c " , cb.getChessColor(new Point(i, j)).getSymbol()) ;
//             System.out.println() ;
//         }
//         System.out.println("After Init of ChessBoard") ;
//         System.out.printf("Black player: %s\nWhite player: %s\n", 
//             cb.getPlayer(ChessColor.BLACK).getName(), 
//             cb.getPlayer(ChessColor.WHITE).getName());
//         System.out.printf("Black player initial chess count: %d\n", 
//             cb.getPlayer(ChessColor.BLACK).getChessNumber());
//         System.out.printf("White player initial chess count: %d\n", 
//             cb.getPlayer(ChessColor.WHITE).getChessNumber());
//     }
// }