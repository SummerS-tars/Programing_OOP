package top.thesumst;

import top.thesumst.core.container.GameContainer;
import top.thesumst.tools.*;

public class Main 
{
    public static void main(String[] args) 
    {
        gameMotd();
        GameContainer gameContainer = new GameContainer();
        gameContainer.runGame();
    }

    private static void gameMotd()
    {
        PrintTools.clearConsole();
        System.out.println("欢迎来到各种棋类游戏！");
        PauseTools.pause("请按回车键以继续");
        PrintTools.clearConsole();
    }
}
