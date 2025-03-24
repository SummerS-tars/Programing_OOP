enum Spiciness 
{
    NOT, MILD, MEDIUM, HOT, FLAMING
}

public class EnumTest 
{
    public static void main(String[] args) 
    {
        /**
         * * Enum.values()方法返回一个包含全部枚举值的数组
         * * Enum.ordinal()方法返回一个int值，这是每个枚举实例在声明时的次序
         */
        for(Spiciness s : Spiciness.values())
        {
            System.out.println(s + ", ordinal " + s.ordinal());
        }
    }
}

/**
 * NOT, ordinal 0
 * MILD, ordinal 1
 * MEDIUM, ordinal 2
 * HOT, ordinal 3
 * FLAMING, ordinal 4
 */