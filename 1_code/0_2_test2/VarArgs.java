public class VarArgs 
{
    static void printArray(Object... args) 
    {
        for (Object obj : args) 
        {
            System.out.print(obj + " ");
        }
        System.out.println();
    }   
    public static void main(String[] args) 
    {
        printArray(Integer.valueOf(47), Float.valueOf(3.14f), Double.valueOf(11.11));
        printArray(47, 3.14F, 11.11);
        printArray("one", "two", "three");
        printArray(new A(), new A(), new A());
        printArray((Object[]) new Integer[] { 1, 2, 3, 4 });
        printArray();
    }
}

class A 
{

}

/**
 * 47 3.14 11.11 
 * 47 3.14 11.11
 * one two three
 * A@72ea2f77 A@33c7353a A@681a9515
 * 1 2 3 4
 */