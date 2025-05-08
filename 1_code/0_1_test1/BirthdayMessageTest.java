import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 生日祝福消息测试程序
 * 用于测试彩色和动画效果的生日祝福消息显示
 */
public class BirthdayMessageTest {
    
    // ANSI 颜色代码常量
    private static final String RESET = "\033[0m";        // 重置所有属性
    private static final String BOLD = "\033[1m";         // 加粗
    private static final String BLINK = "\033[5m";        // 闪烁效果
    private static final String RED = "\033[31m";         // 红色
    private static final String GREEN = "\033[32m";       // 绿色
    private static final String YELLOW = "\033[33m";      // 黄色
    private static final String BLUE = "\033[34m";        // 蓝色
    private static final String MAGENTA = "\033[35m";     // 洋红色
    private static final String CYAN = "\033[36m";        // 青色
    private static final String BRIGHT_RED = "\033[91m";  // 亮红色
    private static final String BRIGHT_GREEN = "\033[92m";// 亮绿色
    private static final String BRIGHT_YELLOW = "\033[93m";// 亮黄色
    private static final String BRIGHT_BLUE = "\033[94m"; // 亮蓝色
    private static final String BRIGHT_MAGENTA = "\033[95m";// 亮洋红色
    private static final String BRIGHT_CYAN = "\033[96m"; // 亮青色
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        
        do {
            // 显示菜单
            System.out.println("\n===== 生日祝福测试程序 =====");
            System.out.println("1. 显示标准祝福消息");
            System.out.println("2. 快速显示祝福消息(无间隔)");
            System.out.println("3. 慢速显示祝福消息(大间隔)");
            System.out.println("4. 只显示框架(测试颜色)");
            System.out.println("5. ASCII模式祝福(无表情符号)");
            System.out.println("0. 退出程序");
            System.out.print("\n请选择: ");
            
            try {
                option = scanner.nextInt();
                
                switch (option) {
                    case 1:
                        clearScreen();
                        showBirthdayMessage(50, 200);
                        break;
                    case 2:
                        clearScreen();
                        showBirthdayMessage(10, 50);
                        break;
                    case 3:
                        clearScreen();
                        showBirthdayMessage(100, 400);
                        break;
                    case 4:
                        clearScreen();
                        showColorTest();
                        break;
                    case 5:
                        clearScreen();
                        showASCIIBirthdayMessage(50, 200);
                        break;
                    case 0:
                        System.out.println("退出程序...");
                        break;
                    default:
                        System.out.println("无效选项，请重新选择");
                }
            } catch (Exception e) {
                System.out.println("输入错误: " + e.getMessage());
                scanner.nextLine(); // 清空输入缓冲
                option = -1;
            }
        } while (option != 0);
        
        scanner.close();
    }
    
    /**
     * 显示彩色祝福测试
     */
    private static void showColorTest() {
        System.out.println("===== 颜色测试 =====");
        System.out.println(RED + "红色文本" + RESET);
        System.out.println(GREEN + "绿色文本" + RESET);
        System.out.println(YELLOW + "黄色文本" + RESET);
        System.out.println(BLUE + "蓝色文本" + RESET);
        System.out.println(MAGENTA + "洋红色文本" + RESET);
        System.out.println(CYAN + "青色文本" + RESET);
        System.out.println(BRIGHT_RED + "亮红色文本" + RESET);
        System.out.println(BRIGHT_GREEN + "亮绿色文本" + RESET);
        System.out.println(BRIGHT_YELLOW + "亮黄色文本" + RESET);
        System.out.println(BRIGHT_BLUE + "亮蓝色文本" + RESET);
        System.out.println(BRIGHT_MAGENTA + "亮洋红色文本" + RESET);
        System.out.println(BRIGHT_CYAN + "亮青色文本" + RESET);
        System.out.println(BOLD + "加粗文本" + RESET);
        System.out.println(BLINK + "闪烁文本" + RESET);
        System.out.println(BOLD + BRIGHT_RED + "加粗亮红色文本" + RESET);
        System.out.println(BLINK + BRIGHT_GREEN + "闪烁亮绿色文本" + RESET);
        
        System.out.println("\n按Enter键继续...");
        new Scanner(System.in).nextLine();
    }
    
    /**
     * 逐步显示生日祝福消息
     * @param charDelay 字符之间的延迟(毫秒)
     * @param lineDelay 行之间的延迟(毫秒)
     */
    private static void showBirthdayMessage(int charDelay, int lineDelay) {
        // 准备彩色祝福信息
        List<String> birthdayWishes = Arrays.asList(
            BRIGHT_YELLOW + BOLD + "🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂" + RESET,
            BLINK + BRIGHT_MAGENTA + BOLD + "✨  今天是您的生日！  ✨" + RESET,
            BLINK + BRIGHT_RED + BOLD + "🎉🎉🎉 生日快乐！ 🎉🎉🎉" + RESET,
            "",
            CYAN + "🖥️ 写" + BRIGHT_CYAN + "Code" + CYAN + "字字生风 💨" + RESET,
            GREEN + "🐛 排" + BRIGHT_GREEN + "Bugs" + GREEN + "个个顺利 ✅" + RESET,
            BLUE + "🏃 运动要多一点 💪" + RESET,
            YELLOW + "💇 头发掉少一点 ✨" + RESET,
            BRIGHT_RED + "🍚 饭多吃点 🍲" + RESET,
            MAGENTA + "🌙 夜少熬点 😴" + RESET,
            "",
            BLINK + BRIGHT_YELLOW + BOLD + "🎊🎊🎊 20岁快乐喵 🎊🎊🎊" + RESET,
            "",
            BRIGHT_GREEN + "(ﾉ≧∀≦)ﾉ " + BRIGHT_YELLOW + "⭐ " + BRIGHT_CYAN + "ヾ(≧▽≦*)o" + RESET,
            BRIGHT_MAGENTA + "⸜(^ω^)⸝ " + BRIGHT_BLUE + "✧*。 " + BRIGHT_RED + "(≧ヮ≦)" + RESET,
            BRIGHT_CYAN + "ヽ(•̀ω•́ )ゝ " + BRIGHT_YELLOW + "✧ " + BRIGHT_GREEN + "╰(°▽°)╯" + RESET,
            "",
            BRIGHT_YELLOW + BOLD + "🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂🎂" + RESET
        );
        
        try {
            // 逐行打印
            for (String line : birthdayWishes) {
                // 逐字打印每一行
                for (int i = 0; i < line.length(); i++) {
                    System.out.print(line.charAt(i));
                    Thread.sleep(charDelay); // 可自定义的字符间隔
                }
                System.out.println(); // 换行
                Thread.sleep(lineDelay); // 可自定义的行间隔
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("祝福显示被中断");
        }
        
        // 完成祝福后等待一会儿再继续
        try {
            Thread.sleep(1500);
            System.out.println("\n按Enter键继续...");
            new Scanner(System.in).nextLine();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 显示ASCII版本的生日祝福消息（无表情符号）
     * @param charDelay 字符之间的延迟(毫秒)
     * @param lineDelay 行之间的延迟(毫秒)
     */
    private static void showASCIIBirthdayMessage(int charDelay, int lineDelay) {
        // 准备ASCII祝福信息（无表情符号）
        List<String> birthdayWishes = Arrays.asList(
            BRIGHT_YELLOW + BOLD + "***************************" + RESET,
            BLINK + BRIGHT_MAGENTA + BOLD + "**  今天是您的生日！  **" + RESET,
            BLINK + BRIGHT_RED + BOLD + "*** 生日快乐！ ***" + RESET,
            "",
            CYAN + ">> 写" + BRIGHT_CYAN + "Code" + CYAN + "字字生风 <<" + RESET,
            GREEN + ">> 排" + BRIGHT_GREEN + "Bugs" + GREEN + "个个顺利 <<" + RESET,
            BLUE + ">> 运动要多一点 <<" + RESET,
            YELLOW + ">> 头发掉少一点 <<" + RESET,
            BRIGHT_RED + ">> 饭多吃点 <<" + RESET,
            MAGENTA + ">> 夜少熬点 <<" + RESET,
            "",
            BLINK + BRIGHT_YELLOW + BOLD + "*** 20岁快乐喵 ***" + RESET,
            "",
            BRIGHT_GREEN + "\\(^o^)/ " + BRIGHT_YELLOW + "* " + BRIGHT_CYAN + "(^_^)/" + RESET,
            BRIGHT_MAGENTA + "(^_^) " + BRIGHT_BLUE + "*。 " + BRIGHT_RED + "(^-^)" + RESET,
            BRIGHT_CYAN + "\\(^-^)/ " + BRIGHT_YELLOW + "* " + BRIGHT_GREEN + "\\(^o^)/" + RESET,
            "",
            BRIGHT_YELLOW + BOLD + "***************************" + RESET
        );
        
        try {
            // 逐行打印
            for (String line : birthdayWishes) {
                // 逐字打印每一行
                for (int i = 0; i < line.length(); i++) {
                    System.out.print(line.charAt(i));
                    Thread.sleep(charDelay); // 可自定义的字符间隔
                }
                System.out.println(); // 换行
                Thread.sleep(lineDelay); // 可自定义的行间隔
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("祝福显示被中断");
        }
        
        // 完成祝福后等待一会儿再继续
        try {
            Thread.sleep(1500);
            System.out.println("\n按Enter键继续...");
            new Scanner(System.in).nextLine();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 清除屏幕
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
