class FinalTest {
    public final int a ;

    public FinalTest(int a) {
        this.a = a;
    }

    public void foo(final int a) {
        a ++ ;
        System.out.println(a);
    }
}

public class Test3{
    public static void main(String[] args) {
        FinalTest test1 = new FinalTest(2);
        System.out.println(test1.a);
        test1.foo(3);
    }
}