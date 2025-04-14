# PRE 代码预评审报告

---

- [1. Part1 Lab5代码分析](#1-part1-lab5代码分析)
    - [1.1. 整体结构分析](#11-整体结构分析)
    - [1.2. 核心代码分析](#12-核心代码分析)
    - [1.3. 优点](#13-优点)
    - [1.4. 不足](#14-不足)
- [2. Part2 Lab5改进建议](#2-part2-lab5改进建议)
    - [2.1. 建议1：重构游戏状态管理](#21-建议1重构游戏状态管理)
    - [2.2. 建议2：增强可测试性](#22-建议2增强可测试性)
- [3. Part3 Lab2到Lab5的转换分析](#3-part3-lab2到lab5的转换分析)
    - [3.1. Part3.1 Lab2到Lab3的转换分析](#31-part31-lab2到lab3的转换分析)
    - [3.2. Part3.2 Lab3到Lab4的转换分析](#32-part32-lab3到lab4的转换分析)
    - [3.3. Part3.3 Lab4到Lab5的转换分析](#33-part33-lab4到lab5的转换分析)
- [4. Part4 代码设计对图形化界面的影响分析](#4-part4-代码设计对图形化界面的影响分析)

---

## 1. Part1 Lab5代码分析

### 1.1. 整体结构分析

Lab5实现了一个完整的五子棋游戏系统，其主要结构如下：

- **棋盘类**：负责棋盘状态的维护和展示
- **棋子类**：封装了棋子的属性和行为
- **玩家类**：处理玩家输入和策略
- **游戏管理类**：协调整体游戏流程
- **界面类**：处理用户交互

### 1.2. 核心代码分析

```java
// 棋盘类示例
public class Board {
    private Piece[][] pieces;
    private int size;
    
    public Board(int size) {
        this.size = size;
        this.pieces = new Piece[size][size];
    }
    
    public boolean placePiece(int row, int col, PieceColor color) {
        // 实现放置棋子的逻辑
    }
    
    public boolean checkWin(int row, int col) {
        // 实现胜利判断逻辑
    }
}
```

### 1.3. 优点

1. **良好的封装性**：各个类的职责划分明确，数据和行为被适当封装
2. **可扩展性**：采用了接口和抽象类设计，便于扩展新功能
3. **代码复用**：通过继承和组合实现了代码复用，减少了冗余

### 1.4. 不足

1. **耦合性问题**：部分组件之间存在较强的依赖关系
2. **异常处理不完善**：缺乏对边界情况的全面考虑

## 2. Part2 Lab5改进建议

### 2.1. 建议1：重构游戏状态管理

**问题**：目前游戏状态管理分散在多个类中，导致状态变化时需要修改多处代码。

**改进建议**：引入状态模式，将游戏状态抽象为独立的类。

**示例代码**：

```java
// 游戏状态接口
public interface GameState {
    void handleInput(Game game, int row, int col);
    void update(Game game);
    void render(Game game);
}

// 具体状态实现
public class PlayingState implements GameState {
    @Override
    public void handleInput(Game game, int row, int col) {
        // 游戏进行中的输入处理
    }
    
    @Override
    public void update(Game game) {
        // 更新游戏状态
    }
    
    @Override
    public void render(Game game) {
        // 渲染当前状态
    }
}

// Game类使用状态
public class Game {
    private GameState currentState;
    
    public void setState(GameState state) {
        this.currentState = state;
    }
    
    public void handleInput(int row, int col) {
        currentState.handleInput(this, row, col);
    }
}
```

### 2.2. 建议2：增强可测试性

**问题**：当前代码难以进行单元测试，特别是UI和业务逻辑混合的部分。

**改进建议**：采用MVC架构，将视图、控制器和模型分离。

**示例代码**：

```java
// 模型
public class GameModel {
    private Board board;
    private Player currentPlayer;
    
    public boolean makeMove(int row, int col) {
        // 处理游戏逻辑
    }
}

// 视图
public class GameView {
    public void displayBoard(Board board) {
        // 显示棋盘
    }
    
    public void showMessage(String message) {
        // 显示消息
    }
}

// 控制器
public class GameController {
    private GameModel model;
    private GameView view;
    
    public void processInput(int row, int col) {
        if (model.makeMove(row, col)) {
            view.displayBoard(model.getBoard());
        } else {
            view.showMessage("Invalid move!");
        }
    }
}
```

## 3. Part3 Lab2到Lab5的转换分析

### 3.1. Part3.1 Lab2到Lab3的转换分析

Lab2到Lab3的转换过程中，主要改进了游戏的基础结构，引入了面向对象的思想。

**转换前问题**：

- 代码结构混乱，大量过程式编程
- 棋盘和棋子的表示方式简单，缺乏抽象

**应用的OOP方法**：

- **抽象**：将棋盘和棋子抽象为类
- **封装**：将数据和操作封装在合适的类中

**示例代码对比**：

```java
// Lab2中的代码（过程式）
int[][] board = new int[15][15];
void placeStone(int x, int y, int player) {
    board[x][y] = player;
}

// Lab3中的代码（面向对象）
public class Board {
    private Piece[][] pieces;
    
    public boolean placePiece(int row, int col, PieceColor color) {
        // 实现放置棋子的逻辑
    }
}
```

### 3.2. Part3.2 Lab3到Lab4的转换分析

Lab3到Lab4的转换过程中，主要增加了游戏的交互性和灵活性。

**转换前问题**：

- 游戏逻辑和界面耦合度高
- 难以支持不同类型的玩家（人类/AI）

**应用的OOP方法**：

- **继承**：创建Player基类，派生HumanPlayer和AIPlayer
- **多态**：使用多态处理不同类型玩家的输入方式

**示例代码对比**：

```java
// Lab3中的代码
public class Game {
    public void play() {
        while (!isGameOver()) {
            // 直接处理玩家输入
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            // 处理落子逻辑
        }
    }
}

// Lab4中的代码
public abstract class Player {
    public abstract Move getMove(Board board);
}

public class HumanPlayer extends Player {
    @Override
    public Move getMove(Board board) {
        // 获取人类玩家输入
    }
}

public class Game {
    private Player[] players;
    
    public void play() {
        while (!isGameOver()) {
            Move move = currentPlayer.getMove(board);
            // 处理落子逻辑
        }
    }
}
```

### 3.3. Part3.3 Lab4到Lab5的转换分析

Lab4到Lab5的转换过程中，主要优化了系统架构，提高了可维护性和扩展性。

**转换前问题**：

- 游戏规则硬编码在逻辑中
- 组件间职责边界模糊

**应用的OOP方法**：

- **组合**：使用组合关系重构系统架构
- **接口**：定义清晰的接口规范组件间通信

**示例代码对比**：

```java
// Lab4中的代码
public class Game {
    private Board board;
    
    public boolean checkWin(int row, int col) {
        // 直接在Game中实现复杂的胜利判断逻辑
    }
}

// Lab5中的代码
public interface WinStrategy {
    boolean checkWin(Board board, int row, int col, PieceColor color);
}

public class FiveInARowWinStrategy implements WinStrategy {
    @Override
    public boolean checkWin(Board board, int row, int col, PieceColor color) {
        // 实现五子连珠的胜利判断逻辑
    }
}

public class Game {
    private Board board;
    private WinStrategy winStrategy;
    
    public boolean checkWin(int row, int col, PieceColor color) {
        return winStrategy.checkWin(board, row, col, color);
    }
}
```

## 4. Part4 代码设计对图形化界面的影响分析

现有的代码设计对实现图形化界面有以下影响：

1. **MVC架构的应用**：如果代码已经采用了MVC架构，添加图形界面会相对容易，只需替换View层实现

2. **事件处理机制**：现有代码的事件处理方式会影响图形界面的实现难度。良好的事件模型设计将简化GUI交互的实现

3. **状态管理**：游戏状态管理的设计会直接影响GUI状态的同步和更新机制

4. **解耦程度**：业务逻辑与界面逻辑的解耦程度决定了添加图形界面的工作量

5. **可扩展接口**：是否预留了与UI交互的接口，如观察者模式的应用，将影响GUI实现的灵活性

为实现图形界面，建议进一步优化以下方面：

- 引入观察者模式，使模型变化能通知到界面
- 将输入处理抽象化，以适应鼠标点击等图形界面输入方式
- 设计清晰的状态渲染接口，支持不同的界面表现形式
