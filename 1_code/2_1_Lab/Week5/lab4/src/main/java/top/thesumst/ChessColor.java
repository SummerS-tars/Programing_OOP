package top.thesumst;

enum ChessColor 
{
    WHITE('●'),
    BLACK('○'),
    BLANK('·'),
    VALID('+');

    private final char symbol;

    ChessColor(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

class TestEnum 
{
    public static void main(String[] args) {
        ChessColor color = ChessColor.BLACK;
        System.out.println("Selected color: " + color + " with symbol " + color.getSymbol());
    }
}
