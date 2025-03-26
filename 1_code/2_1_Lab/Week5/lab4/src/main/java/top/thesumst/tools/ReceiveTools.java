package top.thesumst.tools;

import top.thesumst.tools.command.*;
import top.thesumst.tools.input.*;
import top.thesumst.container.*;
import top.thesumst.mode.*;

public class ReceiveTools 
{
    private PrintTools printTools ;

    public ReceiveTools()
    {
        printTools = new PrintTools() ;
    }

    /**
     * * receiveAndExecuteCommand方法，接收并执行命令
     * @param currentGame
     * @param gameList
     * @return CommandResult 命令运行后的结果
     */
    public CommandResult receiveAndExecuteCommand(GameMode currentGame, GameList gameList)
    {
        // * 接受输入
        InputResult input = receiveInput(currentGame, gameList);

        // * 创建命令
        GameCommand command = CommandFactory.createCommand(input);

        if(command == null)
        {
            return CommandResult.failure("无法处理的命令");
        }

        // * 执行命令
        return command.execute(currentGame, gameList);
    }

    /**
     * * receiveInput方法，接收输入，经过处理得到InputResult
     * @param currentGame
     * @param gameList
     * @return
     */
    private InputResult receiveInput(GameMode currentGame, GameList gameList)
    {
        InputParser.setBoardSize(currentGame.getSize());
        String tips = getTips(currentGame, gameList);
        while(true)
        {
            System.out.println(tips);
            String input = printTools.sc.nextLine();

            InputResult result = InputParser.parse(input);

            if(result.getType() == InputType.INVALID)
            {
                PauseTools.pause("无效输入，请输入回车键继续重新输入");
                continue;
            }

            return result;
        }
    }

    /**
     * * getTips方法，获取提示信息
     * @param currentGame
     * @param gameList
     * @return tips 提示信息
     */
    private String getTips(GameMode currentGame, GameList gameList)
    {
        String tips = new String() ;
        tips += "请输入命令: (" ;
        tips += "1.坐标[1A-" + currentGame.getSize() + (char)('A'+currentGame.getSize()-1) +"](支持大小写+乱序) ";
        tips += "2.切换棋盘[1-" + GameList.getGameNumber() + "] ";
        switch(currentGame.getGameMode())
        {
            case "peace":
                tips += "3.增加棋盘(peace/reversi) 4.退出游戏)";
                break;
            case "reversi":
                tips += "3.跳过(pass) 4.增加棋盘(peace/reversi) 5.退出游戏)";
                break;
        }
        return tips;
    }
}
