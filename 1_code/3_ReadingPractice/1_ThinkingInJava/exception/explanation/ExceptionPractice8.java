package exception.explanation;

class MyException1 extends Exception {
    public MyException1() {}
    public MyException1(String message)  { super(message); }
}

class MyException2 extends RuntimeException {
    public MyException2() {}
    public MyException2(String message)  { super(message); }
}

public class ExceptionPractice8 
{
    // * have to declare MyException1 in the method signature
    // * or it will lead to a compile error
    private static void throwMyException() throws MyException1
    {
        throw new MyException1("MyException1");
    }

    // * no need to declare MyException2 in the method signature
    private static void throwMyRuntimeException()
    {
        throw new MyException2("MyException2");
    }

    public static void main(String[] args) 
    {
        try {
            ExceptionPractice8.throwMyException();
        } catch (MyException1 e) {
            System.out.println("Caught MyException1: " + e.getMessage());
        }

        try {
            ExceptionPractice8.throwMyRuntimeException();
        } catch (MyException2 e) {
            System.out.println("Caught MyException2: " + e.getMessage());
        }
    }
}

/**
 * result output:
 * Caught MyException1: MyException1
 * Caught MyException2: MyException2
 */

/**
 * if no explanation in throwMyException() method
 * the compile time error will be:
 * .\ExceptionPractice8.java:19: 错误: 未报告的异常错误MyException1; 必须对其进行捕获或声明以便抛出
*         throw new MyException1("MyException1");
*         ^
* .\ExceptionPractice8.java:32: 错误: 在相应的 try 语句主体中不能抛出异常错误MyException1
*         } catch (MyException1 e) {
*           ^
* 2 个错误
* 错误: 编译失败
 */