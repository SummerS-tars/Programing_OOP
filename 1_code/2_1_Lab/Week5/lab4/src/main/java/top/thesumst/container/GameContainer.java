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
        PrintTools.clearConsole();
        PrintTools.initializePositionsSet(gameList.getGame(currentGameOrder));
        PrintTools.printBoard(gameList.getGame(currentGameOrder));
        PrintTools.printPlayerInfo(gameList.getGame(currentGameOrder));
        PrintTools.printGameList(gameList);
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

            // 接收输入并执行命令
            CommandResult result = receiver.receiveAndExecuteCommand(currentGame, gameList) ;

            // 处理命令结果
            handleCommandResult(result) ;
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
     * 获取当前游戏序号
     * @return 当前游戏序号
     */
    public static int getCurrentGameOrder()
    {
        return currentGameOrder ;
    }

    /**
     * 处理命令执行结果
     */
    private void handleCommandResult(CommandResult result) 
    {
        PrintTools.goToResultPosition();

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