package top.thesumst.terminal;

import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import top.thesumst.container.GameContainer;
import top.thesumst.container.GameList;
import top.thesumst.mode.GameMode;
import top.thesumst.mode.component.ChessBoard;
import top.thesumst.mode.component.Player;
import top.thesumst.type.ChessColor;

import java.awt.Point;

public class TerminalUI {
    private final Screen screen;
    private final TextGraphics textGraphics;
    
    // 布局尺寸常量
    private static final int 
        BOARD_WIDTH = 40,
        INFO_WIDTH = 20,
        LIST_WIDTH = 20,
        INPUT_HEIGHT = 4;

    public TerminalUI() throws IOException {
        Terminal terminal = new DefaultTerminalFactory()
            .setInitialTerminalSize(new TerminalSize(80, 40))
            .createTerminal();
        screen = new TerminalScreen(terminal);
        textGraphics = screen.newTextGraphics();
        screen.startScreen();
    }

    // 主渲染方法
    public void render(GameContainer container) throws IOException {
        TerminalSize size = screen.getTerminalSize();
        
        // 划分区域
        renderChessBoard(container, new TerminalPosition(0, 0)); 
        renderPlayerInfo(container, new TerminalPosition(BOARD_WIDTH, 0));
        renderGameList(container, new TerminalPosition(BOARD_WIDTH+INFO_WIDTH, 0));
        renderInputPanel(container, new TerminalPosition(0, size.getRows()-INPUT_HEIGHT));
        
        screen.refresh();
    }
}
