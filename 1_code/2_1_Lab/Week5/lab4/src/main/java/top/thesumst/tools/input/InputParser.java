package top.thesumst.tools.input;

import top.thesumst.container.GameList;
import top.thesumst.tools.PauseTools;
import top.thesumst.tools.PrintTools;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入解析器 - 负责解析用户输入
 */
public class InputParser 
{
    // 可变大小的棋盘位置格式
    // 匹配 A1-Z99 或 1A-99Z 格式
    private static final Pattern CHESS_POSITION = Pattern.compile(
        "([A-Za-z])([1-9]\\d{0,1})|([1-9]\\d{0,1})([A-Za-z])"
    );
    
    // 棋盘编号格式: 1, 2, 3...
    private static final Pattern BOARD_NUMBER = Pattern.compile("^([1-9]\\d*)$");
    
    // 命令格式: quit, pass, peace, reversi
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(quit|pass|peace|reversi)$", Pattern.CASE_INSENSITIVE);
    
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
            Point position = parsePosition(positionMatcher);
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
                    return InputResult.newGame(command);
            }
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
    private static Point parsePosition(Matcher matcher) 
    {
        String letter = null;
        String number = null;
        
        // 格式A1: 第一组是字母，第二组是数字
        if (matcher.group(1) != null && matcher.group(2) != null) 
        {
            letter = matcher.group(1);
            number = matcher.group(2);
        } 
        // 格式1A: 第三组是数字，第四组是字母
        else 
        {
            letter = matcher.group(4);
            number = matcher.group(3);
        }
        
        // 将坐标转换
        int row = Integer.parseInt(number) - 1;  // 行号从1开始转为0开始
        int col = Character.toLowerCase(letter.charAt(0)) - 'a';  // 列号A-Z转为0-25
        
        return new Point(row, col);
    }
}

class InputParserTestDrive
{
    private static GameList gameList = new GameList();
    
    
    public static void main(String[] args) 
    {
        gameList.addGame("peace");
        gameList.addGame("reversi");
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
        System.out.println("Input 9 " + InputParser.parse("9"));
        System.out.println("Input 3 " + InputParser.parse("3"));
    }
    
    private static void testMoreInputs() 
    {
        System.out.println("Input J10 " + InputParser.parse("J10"));
        System.out.println("Input 15O " + InputParser.parse("15O"));
        System.out.println("Input K12 " + InputParser.parse("K12"));
        System.out.println("Input Z26 " + InputParser.parse("Z26")); // 超出范围
    }
}