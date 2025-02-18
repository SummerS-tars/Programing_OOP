public class _5_operatorTest {
    public static void main(String[] args) {

        System.out.println("算数运算符演示:") ;
        int a = 10 , b = 3 ;
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("a / b = " + (a / b));
        System.out.println("a % b = " + (a % b));
        // 另外注意一个问题就是,java中print或println中输出此类表达式,注意括号的使用,否则`+`会被当做字符串连接符

        System.out.println("a = " + a);
        System.out.println("-> a++") ;
        a ++ ;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        b -- ;
        System.out.println("-> b--") ;
        System.out.println("b = " + b);
        System.out.println("") ;

        System.out.println("`+`运算符的字符串连接演示:") ;
        String str1 = "Hello" , str2 = "World" ; 
        System.out.println("str1 + str2 = " + str1 + str2);
        // 与C/C++的printf逻辑不同,printf中采用参数替换的方式,而println中`+`是字符串连接符
        System.out.println("") ;

        System.out.println("关系运算符演示:") ;
        System.out.println("a = " + a + " , b = " + b) ;
        System.out.println("a > b ? " + (a > b));
        System.out.println("a < b ? " + (a < b));
        System.out.println("a == b ? " + (a == b));
        System.out.println("a != b ? " + (a != b));
        System.out.println("a >= b ? " + (a >= b));
        System.out.println("a <= b ? " + (a <= b));
        System.out.println("") ;

        System.out.println("逻辑运算符演示:") ;
        boolean bool1 = true , bool2 = false ;
        System.out.println("bool1 = " + bool1 + " , bool2 = " + bool2) ;
        System.out.println("bool1 && bool2 ? " + (bool1 && bool2));
        System.out.println("bool1 || bool2 ? " + (bool1 || bool2));
        System.out.println("!bool1 ? " + (!bool1));
        System.out.println("!bool2 ? " + (!bool2));
        System.out.println("") ;

        System.out.println("位运算符演示:") ;
        int c = 60 , d = 13 ;
        System.out.println("c = " + c + " , d = " + d) ;
        System.out.println("c & d = " + (c & d));
        System.out.println("c | d = " + (c | d));
        System.out.println("c ^ d = " + (c ^ d));
        System.out.println("~c = " + (~c));
        System.out.println("") ;

        System.out.println("移位运算符演示:") ;
        System.out.println("c = " + c) ;
        System.out.println("c << 2 = " + (c << 2));
        System.out.println("c >> 2 = " + (c >> 2));
        System.out.println("c >>> 2 = " + (c >>> 2));
        System.out.println("") ;

        System.out.println("赋值运算符演示:") ;
        System.out.println("a = " + a + " , b = " + b) ;
        a += b ;
        System.out.println("a += b -> a = " + a);
        a -= b ;
        System.out.println("a -= b -> a = " + a);
        a *= b ;
        System.out.println("a *= b -> a = " + a);
        a /= b ;
        System.out.println("a /= b -> a = " + a);
        a %= b ;
        System.out.println("a %= b -> a = " + a);
        a <<= b ;
        System.out.println("a <<= b -> a = " + a);
        a >>= b ;
        System.out.println("a >>= b -> a = " + a);
        a &= b ;
        System.out.println("a &= b -> a = " + a);
        a |= b ;
        System.out.println("a |= b -> a = " + a);
        a ^= b ;
        System.out.println("a ^= b -> a = " + a);
        a >>>= b ;
        System.out.println("a >>>= b -> a = " + a);
        System.out.println("") ;

        System.out.println("条件运算符演示:") ;
        int result = (a > b) ? a : b ;
        System.out.println("result = (a > b) ? a : b -> result = " + result);
        System.out.println("") ;

        System.out.println("instanceof运算符演示:") ;
        Integer resultWrapper = result;
        System.out.println("resultWrapper instanceof Integer ? " + (resultWrapper instanceof Integer));
        System.out.println("resultWrapper instanceof Object ? " + (resultWrapper instanceof Object));
        System.out.println("") ;

        System.out.println("类型转换运算符演示:") ;
        double e = 10.5 ;
        int f = (int)e ;
        System.out.println("f = (int)e" ) ;
        System.out.println("e = " + e + " , f = " + f) ;
    }    
}