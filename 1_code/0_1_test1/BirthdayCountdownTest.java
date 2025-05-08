import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * 生日倒计时功能测试类
 * 用于测试倒计时结束时的生日祝福显示效果
 */
public class BirthdayCountdownTest {
    
    // 从TimeCount类复制的常量
    private static final int BIRTH_MONTH = 5; // 5月
    private static final int BIRTH_DAY = 2;   // 2日
    
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
    
    private static final String[] COLORS = {
        RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, 
        BRIGHT_RED, BRIGHT_GREEN, BRIGHT_YELLOW, BRIGHT_BLUE, BRIGHT_MAGENTA, BRIGHT_CYAN
    };
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        System.out.println(BRIGHT_YELLOW + BOLD + "=== 生日倒计时测试程序 ===" + RESET);
        System.out.println("请选择测试模式：");
        System.out.println("1. 测试最后10秒倒计时");
        System.out.println("2. 直接显示生日祝福");
        System.out.println("3. 测试里程碑提示（最后1分钟）");
        System.out.println("4. 模拟生日当天的倒计时");
        
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close();
        
        switch (choice) {
            case 1:
                testFinalCountdown();
                break;
            case 2:
                showBirthdayMessage();
                break;
            case 3:
                testMilestone();
                break;
            case 4:
                simulateBirthdayCountdown();
                break;
            default:
                System.out.println("无效选择");
        }
    }
    
    /**
     * 测试最后10秒倒计时
     */
    private static void testFinalCountdown() {
        System.out.println(BRIGHT_GREEN + "开始模拟最后10秒倒计时..." + RESET);
        
        try {
            for (int i = 10; i >= 0; i--) {
                String countColor = getRandomColor();
                System.out.print(String.format("\r\033[K距离生日还有: %s%s%d%s 秒     ", 
                             BOLD, countColor, i, RESET));
                
                if (i == 5) {
                    System.out.println();
                    System.out.println(BRIGHT_YELLOW + "[倒计时提醒] 即将到达生日时刻！" + RESET);
                    ring(2);
                }
                
                Thread.sleep(1000);
            }
            
            System.out.println("\n" + BRIGHT_MAGENTA + BOLD + "倒计时结束！生日到啦！" + RESET);
            ring(5, 150);
            Thread.sleep(1000);
            
            showBirthdayMessage();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 测试里程碑提示（最后1分钟）
     */
    private static void testMilestone() {
        System.out.println(BRIGHT_GREEN + "开始模拟最后1分钟倒计时里程碑..." + RESET);
        
        try {
            // 先模拟1分05秒
            System.out.println(YELLOW + "当前模拟时间：距离生日1分05秒" + RESET);
            Thread.sleep(2000);
            
            // 模拟到达1分钟里程碑
            System.out.println("\n" + BRIGHT_RED + BOLD + "[倒计时提醒] 距离生日只剩最后1分钟了！" + RESET);
            ring(4);
            
            // 显示几秒倒计时
            for (int i = 58; i >= 50; i--) {
                String countColor = getRandomColor();
                System.out.print(String.format("\r\033[K距离生日还有: %s%s0%s 分 %s%s%d%s 秒     ", 
                             BOLD, BRIGHT_YELLOW, RESET,
                             BOLD, countColor, i, RESET));
                Thread.sleep(300); // 加快时间以便快速展示
            }
            
            System.out.println("\n\n" + BRIGHT_GREEN + "测试完成！" + RESET);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 模拟生日当天的倒计时
     */
    private static void simulateBirthdayCountdown() {
        System.out.println(BRIGHT_GREEN + "模拟生日当天的情况..." + RESET);
        
        // 获取当前年份
        int currentYear = LocalDate.now().getYear();
        
        // 构造生日日期（今年的生日）
        LocalDate birthdayDate = LocalDate.of(currentYear, BIRTH_MONTH, BIRTH_DAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        
        System.out.println(CYAN + "假设今天是生日: " + birthdayDate.format(formatter) + RESET);
        try {
            Thread.sleep(2000);
            
            System.out.println(BRIGHT_YELLOW + "检测到今天是生日，准备显示生日祝福..." + RESET);
            Thread.sleep(1500);
            
            showBirthdayMessage();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 显示生日祝福消息（从TimeCount类复制）
     */
    private static void showBirthdayMessage() {
        // 清屏
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        // 生日到来时的特殊响铃效果
        ring(5, 150);
        
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
                    Thread.sleep(50); // 每个字符间隔50毫秒
                }
                System.out.println(); // 换行
                
                // 为部分重要行添加提示音
                if (line.contains("今天是您的生日") || 
                    line.contains("生日快乐") || 
                    line.contains("20岁快乐")) {
                    ring(1);
                }
                
                Thread.sleep(200); // 每行之间间隔200毫秒
            }
            
            // 祝福结束时的特殊响铃效果
            Thread.sleep(500);
            ring(3, 300);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("祝福显示被中断");
        }
        
        // 完成祝福后等待一会儿再继续
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n" + GREEN + "测试完成！按Enter键退出..." + RESET);
        new Scanner(System.in).nextLine();
    }
    
    /**
     * 获取随机颜色代码
     */
    private static String getRandomColor() {
        return COLORS[random.nextInt(COLORS.length)];
    }
    
    /**
     * 发出指定次数的响铃声
     */
    private static void ring(int times) {
        ring(times, 200);
    }
    
    /**
     * 发出指定次数和间隔的响铃声
     */
    private static void ring(int times, int intervalMs) {
        new Thread(() -> {
            try {
                for (int i = 0; i < times; i++) {
                    System.out.print('\007');
                    System.out.flush();
                    if (i < times - 1) {
                        Thread.sleep(intervalMs);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
