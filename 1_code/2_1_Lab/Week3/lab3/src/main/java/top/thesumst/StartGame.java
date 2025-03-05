package top.thesumst;

public class StartGame 
{
    /**
     * ! 总程序入口
     * @param args
     */
    public static void main(String[] args) 
    {
        ReversiGame reversiGame = new ReversiGame();
        ReversiGame.gameMotd();
        reversiGame.initGame();
        reversiGame.runGame();
    }
}
