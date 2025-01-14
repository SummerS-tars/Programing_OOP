public class _7_1_switchTest {
    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.println("请输入一个整数: [0 - 9]");
        int num = sc.nextInt();
        switch( num ){
            case 0 :
                System.out.println("零");
                break;
            case 1 :
                System.out.println("一");
                break;
            case 2 :
                System.out.println("二");
                break;
            case 3 :
                System.out.println("三");
                break;
            case 4 :
                System.out.println("四");
                break;
            case 5 :
                System.out.println("五");
                break;
            case 6 :
                System.out.println("六");
                break;
            case 7 :
                System.out.println("七");
                break;
            case 8 :
                System.out.println("八");
                break;
            case 9 :
                System.out.println("九");
                break;
            default :
                System.out.println("输入错误");
        }

        System.out.println("请输入一个字符串: ( 你好 / 再见 / 你是谁 )") ;
        String str ;
        str = sc.next();
        switch( str ){
            case "你好" :
                System.out.println("你好");
                break;
            case "再见" :
                System.out.println("再见");
                break;
            case "你是谁" :
                System.out.println("我是你的朋友");
                break;
            default :
                System.out.println("输入错误");
        }
    }   
}