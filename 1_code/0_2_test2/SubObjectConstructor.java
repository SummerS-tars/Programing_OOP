class Art
{
    Art()
    {
        System.out.println("Art constructor");
    }
}

class Drawing extends Art
{
    Drawing()
    {
        System.out.println("Drawing constructor");
    }
}

class Cartoon extends Drawing
{
    Cartoon()
    {
        System.out.println("Cartoon constructor");
    }

    public static void main(String[] args) 
    {
        Cartoon x = new Cartoon();   
    }
}

/**
 * Art constructor
 * Drawing constructor
 * Cartoon constructor
 */

class A
{
    A()
    {
        System.out.println("A constructor");
    }
}

class B
{
    B()
    {
        System.out.println("B constructor");
    }
}

class C extends A
{
    C()
    {
        System.out.println("C constructor");
    }
    B b = new B();

    public static void main(String[] args) 
    {
        C c = new C();   
        System.out.println(c) ;
    }
}

/**
 * A constructor
 * B constructor
 * C constructor
 * C@27716f4
 */

// ? 和预期不太一样？为什么没有A的构建提示？
// * 可能时缓存问题，重新编译后解决
