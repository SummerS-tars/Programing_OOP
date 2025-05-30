package top.thesumst.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.GUICommandProvider;
import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;
import top.thesumst.core.loop.GUIGameLoop;
import top.thesumst.view.gui.GUIView;
import top.thesumst.view.ViewFactory;

/**
 * Stage 5 集成测试
 * 测试GUI命令集成的核心功能
 */
public class Stage5IntegrationTest {

    private GameList gameList;
    private GUICommandProvider commandProvider;
    private GUIView guiView;
    private GUIGameLoop gameLoop;

    @BeforeEach
    void setUp() {
        gameList = new GameList("测试玩家1", "测试玩家2");
        commandProvider = new GUICommandProvider();
    }

    @Test
    void testGUICommandProviderInitialization() {
        assertNotNull(commandProvider, "GUICommandProvider should be initialized");
        assertEquals(CommandProviderMode.GUI, commandProvider.getMode(), "Command provider should be in GUI mode");
    }

    @Test
    void testGUIViewCreation() {
        try {
            guiView = (GUIView) ViewFactory.getView("gui");
            assertNotNull(guiView, "GUIView should be created by ViewFactory");
        } catch (Exception e) {
            fail("ViewFactory should create GUIView without errors: " + e.getMessage());
        }
    }

    @Test
    void testGUIGameLoopCreation() {
        try {
            guiView = new GUIView();
            gameLoop = new GUIGameLoop(gameList, commandProvider, guiView);
            assertNotNull(gameLoop, "GUIGameLoop should be created successfully");
        } catch (Exception e) {
            fail("GUIGameLoop creation should not fail: " + e.getMessage());
        }
    }

    @Test
    void testCommandProviderCommandQueue() {
        // 测试命令提供者的命令队列功能
        commandProvider.offerCommand("go 3 3");
        
        // 验证命令是否被正确提交
        assertTrue(commandProvider.hasCommand(), "Command provider should have commands after submission");
    }

    @Test
    void testGUIViewControllerBinding() {
        guiView = new GUIView();
        
        // 测试setController方法
        assertDoesNotThrow(() -> {
            guiView.setController(null); // 测试设置null controller不会抛出异常
        }, "Setting controller should not throw exception");
    }

    @Test
    void testCommandProviderTestingMode() {
        // 测试setInputBuffer方法（用于测试）
        assertDoesNotThrow(() -> {
            commandProvider.setInputBuffer("test command");
        }, "setInputBuffer should work for testing");
    }
}
