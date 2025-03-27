package top.thesumst.container;

import top.thesumst.tools.*;
import top.thesumst.mode.*;
import top.thesumst.tools.command.*;

public class GameContainer 
{
    private GameList gameList ;
    private static int currentGameOrder ;
    private boolean isRunning ;

    public GameContainer()
    {
        gameList = new GameList() ;
        currentGameOrder = 1 ;
        isRunning = true ;
    }

    /**
     * ! 游戏主循环
     */
    public void runGame()
    {
        ReceiveTools receiver = new ReceiveTools() ;

        while(isRunning)
        {
            // 获取当前游戏
            GameMode currentGame = gameList.getGame(currentGameOrder) ;

            // 显示游戏信息
            displayGameState(currentGame) ; // TODO 游戏信息显示模块

            // 接收输入并执行命令
            CommandResult result = receiver.receiveAndExecuteCommand(currentGame, gameList) ;

            // 处理命令结果
            handleCommandResult(result) ; // TODO 命令结果处理模块
        }
    }

    /**
     * 切换游戏
     * @param order
     */
    public static void switchGameOrder(int order)
    {
        currentGameOrder = order ;
    }

    /**
     * 显示当前游戏状态
     */
    private void displayGameState(GameMode game) {
        PrintTools.clearConsole();
        
        // 显示游戏列表
        System.out.println("----- 游戏列表 -----");
        for (int i = 1; i <= GameList.getGameNumber(); i++) 
        {
            GameMode g = gameList.getGame(i);
            String current = (i == currentGameOrder) ? "[当前] " : "       ";
            System.out.printf("%s\n", current + g);
        }
        System.out.println("--------------------");
        
        // 显示当前游戏状态
        game.printBoard();
        game.printPlayerInfo();   
    }

    /**
     * 处理命令执行结果
     */
    private void handleCommandResult(CommandResult result) 
    {
        // 处理失败情况
        if (!result.isSuccess()) 
        {
            PauseTools.pause(result.getMessage() + "，请按回车键以重新输入...");
            return;
        }
        
        // 显示成功消息
        PauseTools.pause(result.getMessage() + " 请按回车键以继续");

        // 处理退出命令
        if (result.shouldQuit()) 
        {
            isRunning = false;
            return;
        }
    }

    public static void main(String[] args) 
    {
        // 创建游戏容器
        GameContainer container = new GameContainer() ;


        container.runGame() ;
    }
}