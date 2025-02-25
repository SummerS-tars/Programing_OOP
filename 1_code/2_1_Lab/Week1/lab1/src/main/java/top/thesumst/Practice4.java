package top.thesumst;

import java.util.Scanner ;

public class Practice4 
{
    public static void main( String[] args ) 
    {
        RandomNumberQuiz game = new RandomNumberQuiz() ;
        game.runGame();
    }
}

class RandomNumberQuiz
{
    private final int MIN_NUMBER = 0 ;
    private final int MAX_NUMBER = 100 ;
    private int goalNumber ;
    private int guessTime ;

    // * 游戏运行入口
    public void runGame()
    {
        RandomNumberQuiz game = new RandomNumberQuiz() ;
        game.initializeGame(game);
        game.guessNumber(game);
    }

    private void initializeGame(RandomNumberQuiz game)
    {
        game.goalNumber = (int)(Math.random() * 100) ;
        game.guessTime = 0 ;
    }

    // * 游戏运行本体
    private void guessNumber(RandomNumberQuiz game)
    {
        Scanner sc = new Scanner(System.in) ;
        int attempt = -1 ;

        // * 循环猜数，猜出数后退出
        while( attempt != game.goalNumber)
        {
            System.out.printf("please enter a number (%d - %d) : " , game.MIN_NUMBER , game.MAX_NUMBER) ;
            int t = sc.nextInt() ;
            game.guessTime++ ;
            if( t >= game.MIN_NUMBER && t<= game.MAX_NUMBER)
            {
                attempt = t ;
                if( attempt == game.goalNumber )
                {
                    System.out.println("goal! the number is " + game.goalNumber) ;
                    System.out.println("guess times " + game.guessTime);
                    break ;
                }
                else if(attempt > game.goalNumber) System.out.println("your guess bigger than goal") ;
                else System.out.println("your guess smaller than goal") ;
            }
            else System.out.println("unavailable guess number!") ;
        }

        sc.close();
    }
}
