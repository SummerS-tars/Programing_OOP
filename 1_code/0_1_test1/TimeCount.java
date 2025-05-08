import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeCount 
{
    private static final int BIRTH_MONTH = 5; // 5月
    private static final int BIRTH_DAY = 2;   // 2日
    private static boolean birthdayMessageShown = false; // 记录生日消息是否已显示
    
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
    
    // 记录上一次的倒计时里程碑
    private static long lastMilestoneSeconds = Long.MAX_VALUE;
    
    public static void main(String[] args) 
    {
        System.out.println("生日倒计时程序开始运行...");
        ring(1); // 启动时响铃一次
        
        // 创建定时任务执行器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // 每秒执行一次倒计时更新
        scheduler.scheduleAtFixedRate(() -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDate today = now.toLocalDate();
            
            // 检查是否是分钟的开始（秒数为0）
            if (now.getSecond() == 0) {
                System.out.println("");
                System.out.println("[分钟提醒] 现在是 " + now.getHour() + ":" + 
                                  String.format("%02d", now.getMinute()) + " 整");
                ring(1); // 每分钟整点响铃一次
            }
            
            // 计算今年的生日
            LocalDate birthdayThisYear = LocalDate.of(today.getYear(), Month.of(BIRTH_MONTH), BIRTH_DAY);
            
            // 如果今年的生日已经过了，计算明年的生日
            if (today.isAfter(birthdayThisYear)) {
                birthdayThisYear = LocalDate.of(today.getYear() + 1, Month.of(BIRTH_MONTH), BIRTH_DAY);
            }
            
            // 如果今天是生日的当天且还没有显示过生日消息
            if (today.equals(birthdayThisYear) && !birthdayMessageShown) {
                birthdayMessageShown = true; // 设置标志，避免重复显示
                showBirthdayMessage(); // 显示生日祝福
                return;
            }
            
            // 计算准确的剩余时间
            LocalDateTime birthDateTime = LocalDateTime.of(birthdayThisYear, LocalTime.MIDNIGHT);
            long totalSeconds = ChronoUnit.SECONDS.between(now, birthDateTime);
            
            if (totalSeconds <= 0) {
                // 如果秒数为负，说明计算有误，重新计算到明年的生日
                birthDateTime = LocalDateTime.of(
                    LocalDate.of(today.getYear() + 1, Month.of(BIRTH_MONTH), BIRTH_DAY), 
                    LocalTime.MIDNIGHT);
                totalSeconds = ChronoUnit.SECONDS.between(now, birthDateTime);
            }
            
            // 从总秒数计算天、时、分、秒
            long days = totalSeconds / (24 * 3600);
            long hours = (totalSeconds % (24 * 3600)) / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;
            
            // 检查是否达到特定的倒计时里程碑
            checkCountdownMilestones(totalSeconds);
            
            // 清除当前行并显示新的倒计时信息（不使用\r，避免光标闪烁）
            String daysColor = getRandomColor();
            String hoursColor = getRandomColor();
            String minutesColor = getRandomColor();
            String secondsColor = getRandomColor();
            
            System.out.print(String.format("\033[K距离下一个生日还有: %s%s%d%s 天 %s%s%02d%s 小时 %s%s%02d%s 分钟 %s%s%02d%s 秒     ", 
                             BOLD, daysColor, days, RESET,
                             BOLD, hoursColor, hours, RESET,
                             BOLD, minutesColor, minutes, RESET,
                             BOLD, secondsColor, seconds, RESET));
            
            // 将光标移回行首
            System.out.print("\033[G");
            
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    /**
     * 获取随机颜色代码
     */
    private static String getRandomColor() {
        return COLORS[random.nextInt(COLORS.length)];
    }
    
    /**
     * 发出指定次数的响铃声
     * @param times 响铃次数
     */
    private static void ring(int times) {
        ring(times, 200); // 默认间隔200毫秒
    }
    
    /**
     * 发出指定次数和间隔的响铃声
     * @param times 响铃次数
     * @param intervalMs 响铃间隔(毫秒)
     */
    private static void ring(int times, int intervalMs) {
        new Thread(() -> {
            try {
                for (int i = 0; i < times; i++) {
                    System.out.print('\007'); // ASCII BEL字符
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
    
    /**
     * 检查倒计时是否达到特定的里程碑
     * @param totalSeconds 剩余总秒数
     */
    private static void checkCountdownMilestones(long totalSeconds) {
        // 定义里程碑时间点(秒)
        final long ONE_DAY = 24 * 3600;
        final long TWELVE_HOURS = 12 * 3600;
        final long ONE_HOUR = 3600;
        final long THIRTY_MINUTES = 1800;
        final long TEN_MINUTES = 600;
        final long ONE_MINUTE = 60;
        
        if (lastMilestoneSeconds > ONE_DAY && totalSeconds <= ONE_DAY) {
            System.out.println("\n" + BRIGHT_YELLOW + BOLD + "[倒计时提醒] 距离生日只剩最后24小时了！" + RESET);
            ring(3);
        } else if (lastMilestoneSeconds > TWELVE_HOURS && totalSeconds <= TWELVE_HOURS) {
            System.out.println("\n" + BRIGHT_YELLOW + BOLD + "[倒计时提醒] 距离生日只剩最后12小时了！" + RESET);
            ring(2);
        } else if (lastMilestoneSeconds > ONE_HOUR && totalSeconds <= ONE_HOUR) {
            System.out.println("\n" + BRIGHT_GREEN + BOLD + "[倒计时提醒] 距离生日只剩最后1小时了！" + RESET);
            ring(3);
        } else if (lastMilestoneSeconds > THIRTY_MINUTES && totalSeconds <= THIRTY_MINUTES) {
            System.out.println("\n" + BRIGHT_GREEN + BOLD + "[倒计时提醒] 距离生日只剩最后30分钟了！" + RESET);
            ring(2);
        } else if (lastMilestoneSeconds > TEN_MINUTES && totalSeconds <= TEN_MINUTES) {
            System.out.println("\n" + BRIGHT_CYAN + BOLD + "[倒计时提醒] 距离生日只剩最后10分钟了！" + RESET);
            ring(3);
        } else if (lastMilestoneSeconds > ONE_MINUTE && totalSeconds <= ONE_MINUTE) {
            System.out.println("\n" + BRIGHT_RED + BOLD + "[倒计时提醒] 距离生日只剩最后1分钟了！" + RESET);
            ring(4);
        }
        
        // 更新上次里程碑时间
        lastMilestoneSeconds = totalSeconds;
    }
    
    /**
     * 逐步显示生日祝福消息
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
        
        System.out.println("\n按Ctrl+C退出程序...");
    }
}

class Date
{
    private int year ;
    private int month ;
    private int day ;

    public Date(int year, int month, int day) 
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String toString()
    {
        return String.format("%4d 年 %2d 月 %2d 日", year, month, day);
    }

    public int getYear() 
    {
        return year;
    }
    public void setYear(int year) 
    {
        this.year = year;
    }
    public int getMonth() 
    {
        return month;
    }
    public void setMonth(int month) 
    {
        this.month = month;
    }
    public int getDay() 
    {
        return day;
    }
    public void setDay(int day) 
    {
        this.day = day;
    }
}

class Time
{
    int hour ;
    int minute ;
    int second ;

    public Time(int hour, int minute, int second) 
    {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String toString()
    {
        return String.format("%2d : %2d : %2d :", hour, minute, second);
    }

    public int getHour() 
    {
        return hour;
    }
    public void setHour(int hour) 
    {
        this.hour = hour;
    }
    public int getMinute() 
    {
        return minute;
    }
    public void setMinute(int minute) 
    {
        this.minute = minute;
    }
    public int getSecond() 
    {
        return second;
    }
    public void setSecond(int second) 
    {
        this.second = second;
    }
}