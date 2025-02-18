public class _6_inputTest {
    public static void main(String[] args) {
        // 创建一个Scanner对象,用于接收用户输入
        java.util.Scanner sc = new java.util.Scanner(System.in);


        System.out.print("请输入一个整数:");
        // 从键盘接收数据
        int num = sc.nextInt(); // nextInt()方法表示接收一个整数,因为默认接受的是字符串,所以需要转换
        System.out.println("您输入的整数是: " + num);

        sc.close(); // 关闭Scanner对象
    }
}