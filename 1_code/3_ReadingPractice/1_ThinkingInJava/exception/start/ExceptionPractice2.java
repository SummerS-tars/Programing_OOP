package exception.start;

public class ExceptionPractice2 
{
    public static void main(String[] args) 
    {
        try {
            // Create an instance of AClass
            AClass a = null;
            // Call the method AMethod
            a.AMethod();
        } catch (Exception e) {
            System.err.println("Caught an exception: " + e.getMessage());
        }
    }
}   

class AClass
{
    public void AMethod()
    {
        System.out.println("AClass constructor");
    }
}

/**
 * result output:
 * Caught an exception: Cannot invoke "exception.part1.AClass.AMethod()" because "<local1>" is null
 */