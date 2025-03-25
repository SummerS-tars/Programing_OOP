package top.thesumst;

import java.util.Scanner;

public class PauseUtil {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void pause() {
        pause("按回车键继续...");
    }
    
    public static void pause(String message) {
        System.out.println(message);
        scanner.nextLine();
    }
    
    // 程序结束时调用此方法关闭Scanner
    public static void close() {
        scanner.close();
    }
    
    public static void main(String[] args) {
        System.out.println("程序第一步...");
        
        pause(); // 暂停
        
        System.out.println("程序第二步...");
        
        pause("请按回车键继续执行最后一步..."); // 带自定义消息的暂停
        
        System.out.println("程序完成！");
        
        close(); // 关闭Scanner
    }
}