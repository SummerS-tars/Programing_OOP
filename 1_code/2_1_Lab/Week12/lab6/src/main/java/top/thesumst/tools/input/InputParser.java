package top.thesumst.tools.input;

import top.thesumst.container.GameList;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.PrintTools;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

/**
 * 输入解析器 - 负责解析用户输入
 */
public class InputParser 
{
    // 棋盘位置格式：1A, 2b, AH, FO ...
    private static final Pattern CHESS_POSITION = Pattern.compile(
        "^([1-9a-f][a-o])$" ,
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern BOMB_POSITION = Pattern.compile(
        "^(@)([1-9a-f][a-o])$" ,
        Pattern.CASE_INSENSITIVE
    );
    
    // 棋盘编号格式: 1, 2, 3...
    private static final Pattern BOARD_NUMBER = Pattern.compile("^([1-9]\\d*)$");
    
    // 命令格式: quit, pass, peace, reversi
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(quit|pass|peace|reversi|gomoku)$", Pattern.CASE_INSENSITIVE);

    // DEMO模式：playback test1.cmd ...
    private static final Pattern PLAYBACK_PATTERN = Pattern.compile("^(playback)\\s+([a-zA-Z0-9_\\-]{1,50}\\.cmd)$");
    
    // 棋盘当前大小
    private static int boardSize = 8;
    
    /**
     * 设置棋盘大小
     * @param size 棋盘大小
     */
    public static void setBoardSize(int size) 
    {
        if (size > 0 && size <= 26) // 限制最大为26（A-Z）
            boardSize = size;
    }

    /**
     * 解析输入字符串
     * @param input 用户输入
     * @return 解析结果
     */
    public static InputResult parse(String input) 
    {
        if (input == null || input.trim().isEmpty()) 
        {
            return InputResult.invalid();
        }
        
        input = input.trim();
        
        // 1. 检查是否是落子位置
        Matcher positionMatcher = CHESS_POSITION.matcher(input);
        if (positionMatcher.matches()) 
        {
            Point position = parsePosition(positionMatcher.group(1));
            // 验证位置是否在棋盘范围内
            if (isValidPosition(position)) {
                return InputResult.chessMove(position);
            } else {
                return InputResult.invalid(); // 超出棋盘范围
            }
        }
        
        // 2. 检查是否是棋盘编号
        Matcher boardMatcher = BOARD_NUMBER.matcher(input);
        if (boardMatcher.matches()) 
        {
            int boardNum = Integer.parseInt(input);
            if (boardNum > 0 && boardNum <= GameList.getGameNumber()) 
            {
                return InputResult.switchBoard(boardNum);
            }
            return InputResult.invalid(); // 无效棋盘编号
        }
        
        // 3. 检查是否是命令
        Matcher commandMatcher = COMMAND_PATTERN.matcher(input);
        if (commandMatcher.matches()) 
        {
            String command = input.toLowerCase();
            switch (command) 
            {
                case "quit":
                    return InputResult.quit();
                case "pass":
                    return InputResult.pass();
                case "peace":
                case "reversi":
                case "gomoku":
                    return InputResult.newGame(command);
            }
        }

        Matcher bombMatcher = BOMB_POSITION.matcher(input);
        if(bombMatcher.matches()) 
        {
            Point position = parsePosition(bombMatcher.group(2));
            // 验证位置是否在棋盘范围内
            if (isValidPosition(position)) {
                return InputResult.useBomb(position);
            }
        }
        
        Matcher playbackMatcher = PLAYBACK_PATTERN.matcher(input);
        if(playbackMatcher.matches()) 
        {
            String fileName = playbackMatcher.group(2);
            return InputResult.playback(fileName);
        }
        
        return InputResult.invalid();
    }
    
    /**
     * 检查位置是否在棋盘范围内
     * @param position 棋盘位置
     * @return 是否有效
     */
    private static boolean isValidPosition(Point position) 
    {
        return position.x >= 0 && position.x < boardSize && 
               position.y >= 0 && position.y < boardSize;
    }
    
    /**
     * 从匹配结果解析棋盘位置
     * @param matcher 正则匹配结果
     * @return 棋盘位置
     */
    private static Point parsePosition(String positionString) 
    {
        // 坐标转换
        int row = Integer.parseInt(Character.toString(positionString.charAt(0)), 16) - 1;  // 行号作为十六进制数读入转为10进制数，范围为0-14
        int col = Character.toLowerCase(positionString.charAt(1)) - 'a';  // 列号A-O转为0-14
        
        return new Point(row, col);
    }
}

class InputParserTestDrive
{
    public static void main(String[] args) 
    {
        InputStream originalIn = System.in;
        String simulatedInput = "test1\n1\n2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        GameList gameList = new GameList();
        System.setIn(originalIn);
    
        gameList.addGame("peace");
        gameList.addGame("reversi");
        gameList.addGame("gomoku");
        System.out.println("Game number: " + GameList.getGameNumber());
        PauseTools.pause();
        PrintTools.clearConsole();

        // 测试默认8x8棋盘
        System.out.println("=== 8x8棋盘测试 ===");
        testInput();
        PauseTools.pause();
        PrintTools.clearConsole();
        
        // 测试10x10棋盘
        System.out.println("\n=== 10x10棋盘测试 ===");
        InputParser.setBoardSize(10);
        testInput();
        PauseTools.pause();
        PrintTools.clearConsole();
        
        // 测试15x15棋盘
        System.out.println("\n=== 15x15棋盘测试 ===");
        InputParser.setBoardSize(15);
        testMoreInputs();
        PauseTools.pause();
        PrintTools.clearConsole();
    }

    private static void testInput() 
    {
        System.out.println("Input A1 " + InputParser.parse("A1"));
        System.out.println("Input H8 " + InputParser.parse("H8"));
        System.out.println("Input 1A " + InputParser.parse("1A"));
        System.out.println("Input 8H " + InputParser.parse("8H"));
        System.out.println("Input 2a " + InputParser.parse("2a"));
        System.out.println("Input f7 " + InputParser.parse("f7"));
        System.out.println("Input @1a " + InputParser.parse("@1a"));
        System.out.println("Input @7C " + InputParser.parse("@7C"));
        System.out.println("Input @6 " + InputParser.parse("@6"));
        System.out.println("Input reversi " + InputParser.parse("reversi"));
        System.out.println("Input gomoku " + InputParser.parse("gomoku"));
        System.out.println("Input playback test1 " + InputParser.parse("playback test1"));
        System.out.println("Input playback test1.cmd " + InputParser.parse("playback test1.cmd"));
        System.out.println("Input 9 " + InputParser.parse("9"));
        System.out.println("Input 3 " + InputParser.parse("3"));
    }
    
    private static void testMoreInputs() 
    {
        System.out.println("Input AB " + InputParser.parse("AB"));
        System.out.println("Input do " + InputParser.parse("do"));
        System.out.println("Input jk " + InputParser.parse("jk"));
        System.out.println("Input f1 " + InputParser.parse("f1"));
        System.out.println("Input abc " + InputParser.parse("abc"));
        System.out.println("Input @fo " + InputParser.parse("@fo"));
        System.out.println("Input @gA " + InputParser.parse("@gA"));
        System.out.println("Input peace " + InputParser.parse("peace"));
        System.out.println("Input playback " + InputParser.parse("playback"));
        System.out.println("Input playback   test2.cmd " + InputParser.parse("playback   test2.cmd"));
        System.out.println("Input @a " + InputParser.parse("@a"));
    }
}