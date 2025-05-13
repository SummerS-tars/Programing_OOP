# Refactor Plan

好的，这四个选定的设计方向非常棒，它们能够很好地协同工作，构建一个健壮且可扩展的应用程序。下面是一个详细的、可操作的重构计划，帮助你逐步完成这项工作。

**最终确认的设计目标：**

1.  **使用 `GameLoop` 类**：将主游戏循环逻辑从 `GameContainer` (或 `Main`) 中分离出来，由一个专门的 `GameLoop` 类负责。
2.  **使用命令接收器 (`CommandProvider`)**：实现灵活的输入流机制，支持控制台、文件回放等。
3.  **引入观察者模式**：解耦模型（游戏状态）和视图（显示），当模型变化时自动更新视图。
4.  **保留CLI模式并为GUI模式预留接口**：确保现有命令行功能完整，同时结构上支持未来平滑过渡到图形用户界面。

---

### 重构计划步骤

#### 阶段 0: 准备工作和项目结构调整

1.  **备份你的项目**：在进行大规模重构之前，务必备份当前可工作的代码。
2.  **确认包结构 (参照之前的建议)**：
    * `top.thesumst.core` (模型: `GameMode`, `GameContainer`, `ChessBoard`, `Player` 等)
    * `top.thesumst.io.input` (输入处理: `CommandProvider` 接口和实现, `InputParser`, `InputResult`, `InputType`)
    * `top.thesumst.command` (命令对象: `GameCommand` 接口和实现, `CommandFactory`, `CommandResult`)
    * `top.thesumst.observer` (观察者模式: `Observer`, `Subject`, `BaseSubject`)
    * `top.thesumst.view.console` (控制台视图: `ConsoleView`)
    * `top.thesumst.loop` (新的包，用于 `GameLoop` 类)
    * `top.thesumst.tools` (通用工具: `PrintTools`, `PauseTools`)
    * `top.thesumst.exception` (自定义异常)
    * `top.thesumst.Main` (程序入口)
3.  **移除旧的输入逻辑的直接调用点**：
    * 暂时注释掉 `Main.java` 中直接调用 `ReceiveTools.receiveCommand()` 的部分。
    * 识别出 `GameContainer.java` 中可能存在的游戏循环控制代码，准备将其移出。

---

#### 阶段 1: 实现观察者模式 (Observer Pattern)

1.  **创建 `observer` 包并定义接口**：
    * `Subject.java`:
        ```java
        package top.thesumst.observer;
        public interface Subject {
            void registerObserver(Observer observer);
            void removeObserver(Observer observer);
            void notifyObservers(Object... args);
        }
        ```
    * `Observer.java`:
        ```java
        package top.thesumst.observer;
        public interface Observer {
            void update(Subject subject, Object... args);
        }
        ```
    * `BaseSubject.java` (提供通用实现):
        ```java
        package top.thesumst.observer;
        import java.util.ArrayList;
        import java.util.List;
        public abstract class BaseSubject implements Subject {
            private final List<Observer> observers = new ArrayList<>();
            private boolean changed = false;
            @Override public void registerObserver(Observer o) { if(o!=null && !observers.contains(o)) observers.add(o); }
            @Override public void removeObserver(Observer o) { observers.remove(o); }
            protected void setChanged() { this.changed = true; }
            protected void clearChanged() { this.changed = false; }
            protected boolean hasChanged() { return this.changed; }
            @Override public void notifyObservers(Object... args) {
                if (!hasChanged()) return;
                List<Observer> observersCopy = new ArrayList<>(this.observers);
                for (Observer observer : observersCopy) {
                    observer.update(this, args);
                }
                clearChanged();
            }
        }
        ```

2.  **修改核心模型类成为 `Subject`**：
    * **`GameMode.java`**:
        * 让 `public abstract class GameMode` 继承 `BaseSubject`。
        * 在所有改变游戏状态的方法（如 `makeMove`, `useBomb` (在 `GomokuMode` 中), `switchPlayer` (如果它可以独立改变状态并需要通知), `checkWinCondition` (如果它改变了 `gameOver` 状态)）的末尾，调用 `setChanged();` 和 `notifyObservers("EVENT_TYPE", relevantData);`。
            * `EVENT_TYPE` 可以是 `"MOVE_MADE"`, `"BOMB_USED"`, `"GAME_OVER"`, `"PLAYER_SWITCHED"` 等。
    * **`GameContainer.java`**:
        * 让 `public class GameContainer` 继承 `BaseSubject`。
        * 在其修改内部状态的方法（如 `setCurrentGameId`, `addNewGame`, `initializeDefaultGames` 中每添加一个游戏或设置当前游戏后）的末尾，调用 `setChanged();` 和 `notifyObservers("EVENT_TYPE", relevantData);`。
            * `EVENT_TYPE` 可以是 `"CURRENT_GAME_SWITCHED"`, `"GAME_ADDED"`, `"INITIAL_GAMES_LOADED"`。

3.  **创建 `ConsoleView.java` 作为 `Observer`**：
    * 在 `top.thesumst.view.console` 包下创建 `ConsoleView.java`。
    * 让 `public class ConsoleView implements Observer`。
    * 构造函数接收 `GameContainer` 的引用：`public ConsoleView(GameContainer gameContainer) { this.gameContainer = gameContainer; }`。
    * 实现 `update(Subject subject, Object... args)` 方法：
        * 根据 `subject` 的类型 (是 `GameMode` 还是 `GameContainer`) 和 `args` 中的事件类型来决定如何刷新显示。
        * 调用内部的私有方法 (原 `PrintTools` 的大部分棋盘、状态打印逻辑移到这里) 来重绘控制台。例如 `displayGameScreen(GameMode gameToDisplay)`。
        * 当收到来自 `GameContainer` 的 `"CURRENT_GAME_SWITCHED"` 通知时，`ConsoleView` 需要知道新的 `GameMode` 实例，以便后续正确显示或如果它也直接观察 `GameMode` 的话，进行(反)注册。

4.  **调整 `PrintTools.java`**：
    * 将其职责缩减为纯粹的、不依赖当前游戏完整状态的静态工具方法 (例如，打印特定格式的提示符、打印命令执行的简单消息、清屏 `clearConsole()` 等)。
    * 大部分棋盘和游戏状态的完整打印逻辑应迁移到 `ConsoleView` 中。

---

#### 阶段 2: 实现命令接收器 (`CommandProvider`)

1.  **创建 `io.input` 包并定义接口和类**：
    * `CommandProvider.java` (如之前定义)。
    * `ConsoleCommandProvider.java`：
        * 构造函数接收 `Scanner`。
        * `getNextCommand()` 使用 `scanner.nextLine()`。
    * `FileCommandProvider.java` (用于回放/演示)：
        * 构造函数接收文件路径和 `simulateDelay` 布尔值。
        * `getNextCommand()` 从 `BufferedReader` 逐行读取，并根据 `simulateDelay` 选择性地 `Thread.sleep()`。
        * 包含 `close()` 方法关闭 `BufferedReader`。
    * 旧的 `ReceiveTools.java` 的 `receiveCommand` 方法将被 `ConsoleCommandProvider` 取代。`ReceiveTools` 可以废弃或保留其他无关的静态方法。

---

#### 阶段 3: 实现 `GameLoop` 类

1.  **创建 `loop` 包和 `GameLoop.java`**：
    * `public class GameLoop`。
    * **成员变量**:
        * `private GameContainer gameContainer;`
        * `private CommandProvider commandProvider;`
        * `private ConsoleView consoleView; // 或者更通用的 View 接口`
        * `private boolean isInteractiveMode;`
    * **构造函数**: `public GameLoop(GameContainer gameContainer, CommandProvider commandProvider, ConsoleView consoleView, boolean isInteractiveMode)`。
    * **`public void run()` 方法**:
        * 包含主 `while (gameContainer.isRunning()) { ... }` 循环。
        * **循环内部逻辑**:
            1.  **交互提示**: 如果 `isInteractiveMode`，调用 `PrintTools.promptForInput(gameContainer.getCurrentPlayer());`。
            2.  **获取输入**: `String rawInput = commandProvider.getNextCommand();`。处理 `rawInput == null` (输入结束)。
            3.  **回显命令 (非交互模式)**: 如果不是 `isInteractiveMode`，打印 `rawInput`。
            4.  **解析输入**: `InputResult inputResult = InputParser.parse(rawInput);`。处理解析失败。
            5.  **创建命令**: `GameCommand command = CommandFactory.createCommand(inputResult, gameContainer);`。处理创建失败。
            6.  **管理观察者 (针对游戏切换)**:
                * 获取切换前的 `GameMode gameBeforeExecute = gameContainer.getCurrentGame();`。
                * 判断是否为 `SwitchBoardCommand`。
            7.  **执行命令**: `CommandResult result = command.execute();`。
                * 此时，如果命令改变了 `GameMode` 或 `GameContainer` 的状态，它们内部的 `notifyObservers()` 会被调用，进而触发 `ConsoleView.update()` 来刷新显示。
            8.  **管理观察者 (切换后)**:
                * 获取切换后的 `GameMode gameAfterExecute = gameContainer.getCurrentGame();`。
                * 如果发生成功的游戏切换 (`isSwitchCommand && result.isSuccess() && gameBeforeExecute != gameAfterExecute`):
                    * `if (gameBeforeExecute != null) gameBeforeExecute.removeObserver(consoleView);`
                    * `if (gameAfterExecute != null) gameAfterExecute.registerObserver(consoleView);`
                    * (可选/推荐) 手动触发一次新游戏画面的渲染:
                        `if (gameAfterExecute != null) { gameAfterExecute.setChanged(); gameAfterExecute.notifyObservers("GAME_REFRESH_AFTER_SWITCH"); }`
            9.  **打印命令直接结果**: `PrintTools.printCommandResult(result);` (这个 `PrintTools` 方法现在应该只打印简单的成功/失败消息，而不是整个棋盘)。
            10. **处理暂停**: 如果 `isInteractiveMode`，根据 `result` 和游戏状态决定是否调用 `PauseTools.pause();`。
            11. **处理退出命令**: 如果是 `QuitCommand` 且成功，调用 `gameContainer.stopRunning();`。
            12. **处理游戏结束**: 如果 `gameContainer.isGameOver()`，打印相应信息 (部分可能已由 `ConsoleView` 在 `update` 中处理)，并在交互模式下提示用户。

---

#### 阶段 4: 修改 `Main.java` (程序入口)

1.  **`Main.java` 的职责**：
    * 初始化核心组件：
        * `GameContainer gameContainer = new GameContainer();`
        * `ConsoleView consoleView = new ConsoleView(gameContainer);`
    * 注册初始观察者：
        * `gameContainer.registerObserver(consoleView);`
        * `gameContainer.initializeDefaultGames();` // 这应触发通知，让 `ConsoleView` 渲染初始状态
        * `GameMode initialGame = gameContainer.getCurrentGame();`
        * `if (initialGame != null) initialGame.registerObserver(consoleView);`
    * 根据命令行参数 (`args`) 决定并创建相应的 `CommandProvider` 实例 (`ConsoleCommandProvider` 或 `FileCommandProvider`) 并设置 `isInteractiveMode`。
    * 创建 `GameLoop` 实例，传入所有依赖：
        * `GameLoop gameLoop = new GameLoop(gameContainer, commandProvider, consoleView, isInteractiveMode);`
    * 启动游戏循环：`gameLoop.run();`
    * 循环结束后进行必要的清理 (关闭 `Scanner`, `BufferedReader`, 移除观察者等)。

---

#### 阶段 5: 测试和细化

1.  **单元测试**：为新的/修改的类编写单元测试，特别是：
    * `GameMode` 和 `GameContainer` 的状态改变与通知逻辑。
    * `ConsoleView` 的 `update` 方法在不同事件下的响应。
    * `CommandProvider` 的不同实现。
    * `GameLoop` 的基本流程控制 (可能需要模拟依赖)。
    * `InputParser` 和 `CommandFactory`。
2.  **集成测试**：运行完整的游戏流程，测试：
    * CLI 模式下的所有功能 (下棋、切换、新游戏、炸弹、回放)。
    * 回放模式 (`FileCommandProvider`) 是否流畅，是否不受用户键盘输入干扰，暂停是否被正确跳过。
    * 演示模式 (`FileCommandProvider` with `simulateDelay=true`)。
    * 错误处理和边界条件。
3.  **代码审查和优化**：
    * 检查是否有不必要的耦合。
    * 确保 `notifyObservers` 的参数 `args` 被有效利用，以帮助 `Observer` 更高效地更新。
    * 优化 `ConsoleView` 的渲染逻辑，避免不必要的重复打印。

---

### 对现有代码文件的具体影响 (高层次概述):

* **`Main.java`**: 大幅重写，主要负责初始化和启动 `GameLoop`。
* **`GameContainer.java`**: 移除游戏循环逻辑。增加 `Subject` 实现，并在状态改变时通知。
* **`GameMode.java` (及其子类)**: 增加 `Subject` 实现，并在状态改变时通知。
* **`ReceiveTools.java`**: 很可能被废弃，其功能由 `ConsoleCommandProvider` 替代。
* **`PrintTools.java`**: 职责缩减，部分渲染逻辑移至 `ConsoleView`。
* **`Playback.java`**: 其核心功能由 `FileCommandProvider` 实现。可以废弃或保留为辅助（如录制功能）。
* **命令类 (`GoCommand`, etc.)**: 基本不变，它们仍然操作 `GameContainer` 和 `GameMode`。
* **`InputParser`, `CommandFactory`**: 基本不变。

这个计划比较详尽，你可以分阶段逐步实施。每完成一个阶段，进行充分的测试，确保一切按预期工作，然后再进入下一个阶段。祝你重构顺利！