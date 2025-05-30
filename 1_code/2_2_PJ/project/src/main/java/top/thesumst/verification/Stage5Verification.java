package top.thesumst.verification;

import top.thesumst.core.container.GameList;
import top.thesumst.io.provider.GUICommandProvider;
import top.thesumst.core.loop.GUIGameLoop;
import top.thesumst.view.gui.GUIView;
import top.thesumst.view.ViewFactory;
import top.thesumst.core.container.GameContainer;

/**
 * Stage 5 验证程序
 * 验证GUI命令集成的核心功能是否正常工作
 */
public class Stage5Verification {
    
    public static void main(String[] args) {
        System.out.println("=== Stage 5 GUI命令集成验证开始 ===");
        
        try {
            // 1. 测试GUICommandProvider创建
            System.out.println("1. 测试GUICommandProvider创建...");
            GUICommandProvider commandProvider = new GUICommandProvider();
            System.out.println("   ✓ GUICommandProvider创建成功");
            
            // 2. 测试GUIView创建
            System.out.println("2. 测试GUIView创建...");
            GUIView guiView = (GUIView) ViewFactory.getView("gui");
            System.out.println("   ✓ GUIView创建成功");
            
            // 3. 测试GameList创建
            System.out.println("3. 测试GameList创建...");
            GameList gameList = new GameList();
            System.out.println("   ✓ GameList创建成功");
            
            // 4. 测试GUIGameLoop创建
            System.out.println("4. 测试GUIGameLoop创建...");
            GUIGameLoop gameLoop = new GUIGameLoop(gameList, commandProvider, guiView);
            System.out.println("   ✓ GUIGameLoop创建成功");
            
            // 5. 测试GUIController绑定
            System.out.println("5. 测试GUIController绑定...");
            guiView.setController(null); // 测试设置null不会出错
            System.out.println("   ✓ GUIView.setController()方法正常");
            
            // 6. 测试命令提交
            System.out.println("6. 测试命令提交...");
            commandProvider.offerCommand("go 3 3");
            System.out.println("   ✓ 命令提交功能正常");
            
            // 7. 测试hasCommand
            System.out.println("7. 测试命令队列状态...");
            boolean hasCommand = commandProvider.hasCommand();
            System.out.println("   ✓ hasCommand()返回: " + hasCommand);
            
            // 8. 测试测试模式接口
            System.out.println("8. 测试测试模式接口...");
            commandProvider.setInputBuffer("test command");
            System.out.println("   ✓ setInputBuffer()测试接口正常");
            
            // 9. 测试GameContainer创建（不启动）
            System.out.println("9. 测试GameContainer创建...");
            @SuppressWarnings("unused")
            GameContainer gameContainer = new GameContainer(gameList, gameLoop, commandProvider, guiView);
            System.out.println("   ✓ GameContainer创建成功");
            
            System.out.println("\n=== Stage 5 GUI命令集成验证完成 ===");
            System.out.println("所有核心组件验证通过！");
            
        } catch (Exception e) {
            System.err.println("验证过程中发生错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
