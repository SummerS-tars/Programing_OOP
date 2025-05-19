package top.thesumst.io;

import top.thesumst.core.command.*;
import top.thesumst.core.container.*;
import top.thesumst.core.mode.*;
import top.thesumst.io.input.*;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.PrintTools;

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
        while(true)
        {
            PrintTools.printInputPanel(gameList);

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
}
