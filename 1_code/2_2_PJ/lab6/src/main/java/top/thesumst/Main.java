package top.thesumst;

import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList;
import top.thesumst.core.loop.GameLoop;
import top.thesumst.core.loop.GameLoopFactory;
import top.thesumst.io.provider.BaseCommandProvider;
import top.thesumst.io.provider.CommandProviderFactory;
import top.thesumst.observer.Observer;
import top.thesumst.tools.*;
import top.thesumst.view.ViewFactory;
import top.thesumst.view.console.CLIPrintTools;

public class Main 
{
    static GameContainer gameContainer;
    static GameList gameList;
    static GameLoop gameLoop;
    static Observer observer;
    static BaseCommandProvider cmdProvider;

    /**
     * 游戏主函数
     * @param args
     */
    public static void main(String[] args) 
    {
        gameMotd();
        try{
            observer = ViewFactory.getView(args[0]);
            cmdProvider = CommandProviderFactory.getCommandProvider(args[0]);
            gameList = new GameList();
            gameLoop = GameLoopFactory.getGameLoop(args[0], gameList, cmdProvider, observer);
            gameContainer = new GameContainer(gameList, gameLoop, cmdProvider, observer);
            gameContainer.runGame();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("启动模式参数错误: " + e.getMessage());
            System.out.println("请使用 'cli' 或 'gui' 启动游戏");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("发生了未知错误: " + e.getMessage());
        }
    }

    private static void gameMotd()
    {
        CLIPrintTools.clearConsole();
        System.out.println("欢迎来到各种棋类游戏！");
        PauseTools.pause("请按回车键以继续");
        CLIPrintTools.clearConsole();
    }
}
