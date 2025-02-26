package top.thesumst;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;




public class ReversiGameTest {

    private Player player;
    private ChessBoard chessBoard;

    @Before
    public void setUp() {
        player = new Player();
        chessBoard = new ChessBoard();
    }

    @Test
    public void testPlayerInitialization() {
        player.initPlayer();
        assertTrue(player.getName() != null && !player.getName().isEmpty());
        assertTrue(player.getColor() == ChessBoard.BLACK_CHESS || player.getColor() == ChessBoard.WHITE_CHESS);
    }

    @Test
    public void testChessBoardInitialization() {
        chessBoard.initChessBoard();
        assertEquals(ChessBoard.BLANK, chessBoard.getChessColor(0, 0));
        assertTrue(chessBoard.getBlackTurn());
    }

    @Test
    public void testSetAndGetChessColor() {
        chessBoard.initChessBoard();
        chessBoard.setChessColor(0, 0, ChessBoard.BLACK_CHESS);
        assertEquals(ChessBoard.BLACK_CHESS, chessBoard.getChessColor(0, 0));
    }

    @Test
    public void testSetAndGetBlackTurn() {
        chessBoard.initChessBoard();
        chessBoard.setBlackTurn(false);
        assertTrue(!chessBoard.getBlackTurn());
    }
}