package top.thesumst;

import java.util.*;

public class ReversiGame
{

}

class ChessBoard
{
    Player p1 , p2 ;
}

/**
 * class Player use to 
 */
class Player
{
    private String name ;
    private char color ;
    private int chessNumber ;

    public void initPlayer()
    {
        this.chessNumber = 0 ;
    }

    public void setName()
    {
        try(Scanner sc = new Scanner(System.in))
        {
            
            this.name = sc.nextLine() ;
        }
    }

    public void setColor()
    {
        try(Scanner sc = new Scanner(System.in))
        {
            int option = 0 ;
            while( option == 0 )
            {
                switch (sc.nextInt()) {
                    case 1:
                        this.color = 
                        break;
                
                    default:
                        break;
                }
            }
        }
        }
    }
}