public class constTest {
    public static void main(String[] args){
        System.out.println(100) ;
        System.out.println(3.14) ;
        System.out.print('A') ;
        System.out.println('中') ;
        
        // System.out.println('') ;
        // java中单引号中间必须有且仅有一个字符,否则编译报错,不允许空字符

        System.out.println("\"Hello,"+"World!\"") ;
        System.out.println(true) ;  // 注意:boolean类型的输出,会直接输出true或false

        // 注意:这个输出方式(println)会自动换行另一种(print)则不会
        // 注意:javac会检测源文件的编码模式,所以有中文注释最好用GBK编码方式存储
    }
}
