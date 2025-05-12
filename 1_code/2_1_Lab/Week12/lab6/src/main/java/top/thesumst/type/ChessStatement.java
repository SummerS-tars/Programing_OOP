package top.thesumst.type;

public enum ChessStatement 
{
    WHITE('●'),
    BLACK('○'),
    BLANK('·'),
    VALID('+');

    private final char symbol;

    ChessStatement(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

class TestEnum 
{
    public static void main(String[] args) {
        ChessStatement color = ChessStatement.BLACK;
        System.out.println("Selected color: " + color + " with symbol " + color.getSymbol());
    }
}
