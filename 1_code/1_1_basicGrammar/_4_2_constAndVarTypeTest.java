public class _4_2_constAndVarTypeTest {
    public static void main(String[] args){
        // int a = 10L ;    // 10L会被默认为long类型,不能赋值给int类型
        long a = 10 ;   // 10会被默认为int类型,但仍然可以赋值给long类型
        // int a0 = 10 ;
        // short a1 = a0 ; // 注意:下面可以,但这里不可以
        short a1 = 11 ; // 注意:在不超过short范围内,整数常量(变量不可以)可以直接赋值给short类型(byte同理)
        byte a2 = 12 ;
        // float c = 1.234 ;
        float b = 1.234f ;
        double b1 = 1.234 ;
        float b2 = (float)b1 ;
        double b3 = b2 ;

        int a3 = (int)13.5; // 浮点类型向整数类型转换时,直接去掉小数点后的部分

        System.out.print(a);
        System.out.print(a1);
        System.out.print(a2);
        System.out.print(a3);
        System.out.println();
        System.out.print(b);
        System.out.print(' ');
        System.out.print(b1);
        System.out.print(' ');
        System.out.print(b2);
        System.out.print(' ');
        System.out.print(b3);

        /**
         * 总结:
         * 1. 大部分从低到高的自动转换是允许的
         * 2. 大部分从高到低的自动转换是不允许的
         * 3. 特例:在不超过数据类型的范围情况下,整数常量(变量不可以)可以直接赋值给short类型(byte同理)
         *     3.1 小知识:x = x + 1; // x是byte,会报错,因为x+1会被默认为int类型,不能赋值给byte类型
         *     3.2 小知识:x += 1; // x是byte,不会报错,因为+=会自动进行强制类型转换
         */
    }
}
