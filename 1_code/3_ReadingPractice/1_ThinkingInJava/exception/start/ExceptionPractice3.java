package exception.start;

public class ExceptionPractice3 
{
    public static void main(String[] args) 
    {
        try {
            Integer[] list = {1, 2, 3, 4, 5} ;
            System.out.printf("the %dth element : %d\n", 5, list[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ArrayIndexOutOfBoundsException: " + e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}

/**
 * result output:
 * ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
 * java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
 *         at exception.ExceptionPractice3.main(ExceptionPractice3.java:9)
 */