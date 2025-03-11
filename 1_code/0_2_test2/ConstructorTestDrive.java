public class ConstructorTestDrive
{
    public static void main(String[] args)
    {
        ConstructorTest test = new ConstructorTest() ;
        System.out.println(test.string);
    }
}

class ConstructorTest
{
    public String string ;
}

/**
 * output:
 * null
 */