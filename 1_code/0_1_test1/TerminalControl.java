public class TerminalControl {
    public static void main(String[] args) throws InterruptedException {
        // 清屏
        System.out.print("\033[2J");

        // 移动光标到第5行第10列
        System.out.print("\033[5;10H");
        System.out.print("Hello, World!");

        // 记住当前光标位置
        String saveCursorPosition = "\033[s";
        System.out.print(saveCursorPosition);

        // 移动光标到第10行第10列
        System.out.print("\033[10;10H");
        System.out.print("This is a test.");

        // 恢复光标位置
        String restoreCursorPosition = "\033[u";

        // 等待3秒
        Thread.sleep(3000);

        // 恢复光标位置并覆盖输出
        System.out.print(restoreCursorPosition);
        System.out.print("Overwritten!");

        // 确保输出刷新
        System.out.flush();
    }
}
