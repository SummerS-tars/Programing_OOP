# 棋类游戏代码预评审报告 zqy

---

- [1. Part1: Lab5代码分析](#1-part1-lab5代码分析)
    - [1.1. 整体架构](#11-整体架构)
    - [1.2. 代码片段分析](#12-代码片段分析)
    - [1.3. 优点](#13-优点)
    - [1.4. 不足](#14-不足)
- [2. Part2: Lab5改进建议](#2-part2-lab5改进建议)
    - [2.1. 引入观察者模式优化UI更新](#21-引入观察者模式优化ui更新)
        - [2.1.1. 问题分析](#211-问题分析)
        - [2.1.2. 改进建议](#212-改进建议)
    - [2.2. 采用工厂模式创建不同类型的游戏](#22-采用工厂模式创建不同类型的游戏)
        - [2.2.1. 问题分析](#221-问题分析)
        - [2.2.2. 改进建议](#222-改进建议)
    - [2.3. 策略模式优化规则实现](#23-策略模式优化规则实现)
        - [2.3.1. 问题分析](#231-问题分析)
        - [2.3.2. 改进建议](#232-改进建议)
- [3. Part3: Lab2到Lab5的转换分析](#3-part3-lab2到lab5的转换分析)
    - [3.1. Part3.1: Lab2到Lab3的转换分析](#31-part31-lab2到lab3的转换分析)
        - [3.1.1. 结构变化](#311-结构变化)
        - [3.1.2. OOP方法应用](#312-oop方法应用)
    - [3.2. Part3.2: Lab3到Lab4的转换分析](#32-part32-lab3到lab4的转换分析)
        - [3.2.1. 结构变化](#321-结构变化)
        - [3.2.2. OOP方法应用](#322-oop方法应用)
    - [3.3. Part3.3: Lab4到Lab5的转换分析](#33-part33-lab4到lab5的转换分析)
        - [3.3.1. 结构变化](#331-结构变化)
        - [3.3.2. OOP方法应用](#332-oop方法应用)
- [4. Part4: 对图形化界面的影响分析](#4-part4-对图形化界面的影响分析)
    - [4.1. 有利因素](#41-有利因素)
    - [4.2. 潜在挑战](#42-潜在挑战)
    - [4.3. 改进建议](#43-改进建议)

---

## 1. Part1: Lab5代码分析

### 1.1. 整体架构

Lab5是棋类游戏的最终版本，主要实现了完整的游戏功能，包括多种棋类游戏支持、落子规则校验、胜负判定以及悔棋功能等。代码结构遵循了面向对象的设计原则，主要采用了以下架构：

```txt
Game
 ├── Board
 │    ├── Position
 │    └── Piece
 ├── Player
 ├── Rules
 │    ├── MoveValidator
 │    └── WinChecker
 └── GameHistory
```

### 1.2. 代码片段分析

```java
// 抽象棋盘类，提供了通用的棋盘操作接口
public abstract class Board {
    protected int width;
    protected int height;
    protected Piece[][] pieces;
    
    // 抽象方法，由具体子类实现
    public abstract boolean placePiece(Position pos, Piece piece);
    public abstract Piece getPieceAt(Position pos);
    // ...其他方法
}

// 游戏规则接口
public interface Rules {
    boolean validateMove(Board board, Position pos, Piece piece);
    boolean checkWin(Board board, Position lastMove, Piece piece);
    // ...其他方法
}

// 游戏历史记录，用于实现悔棋功能
public class GameHistory {
    private Stack<Move> moveHistory;
    
    public void recordMove(Move move) {
        moveHistory.push(move);
    }
    
    public Move undoLastMove() {
        if (!moveHistory.isEmpty()) {
            return moveHistory.pop();
        }
        return null;
    }
    // ...其他方法
}
```

### 1.3. 优点

1. **良好的抽象**：通过抽象类和接口定义了棋盘、棋子和规则的通用行为
2. **高内聚低耦合**：各模块职责明确，如规则校验与胜负判断分离
3. **扩展性强**：新增棋类游戏只需实现相应的子类和接口
4. **封装性好**：内部实现细节对外部隐藏，提供清晰的公共API

### 1.4. 不足

1. **部分功能耦合度较高**：游戏流程控制和UI交互逻辑混合
2. **异常处理机制不完善**：缺少系统性的异常处理策略
3. **代码复用不足**：存在部分重复代码，特别是在不同棋类的规则实现中

## 2. Part2: Lab5改进建议

### 2.1. 引入观察者模式优化UI更新

#### 2.1.1. 问题分析

当前游戏状态变化与UI更新紧密耦合，使得游戏逻辑难以独立测试和扩展。

#### 2.1.2. 改进建议

采用观察者模式，让UI组件订阅游戏状态变化：

```java
// 改进前
public class Game {
    public void makeMove(Position pos) {
        // 执行移动
        // ...
        // 直接更新UI
        ui.updateBoard();
        ui.showMessage("移动完成");
    }
}

// 改进后
public class Game implements Observable {
    private List<Observer> observers = new ArrayList<>();
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void notifyObservers(GameEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
    
    public void makeMove(Position pos) {
        // 执行移动
        // ...
        // 通知观察者
        notifyObservers(new MoveEvent(pos, currentPlayer));
    }
}
```

### 2.2. 采用工厂模式创建不同类型的游戏

#### 2.2.1. 问题分析

游戏创建逻辑分散，增加新游戏类型需要修改多处代码。

#### 2.2.2. 改进建议

引入游戏工厂模式：

```java
public interface GameFactory {
    Game createGame();
    Board createBoard();
    Rules createRules();
}

public class GoGameFactory implements GameFactory {
    @Override
    public Game createGame() {
        return new GoGame();
    }
    
    @Override
    public Board createBoard() {
        return new GoBoard(19, 19);
    }
    
    @Override
    public Rules createRules() {
        return new GoRules();
    }
}
```

### 2.3. 策略模式优化规则实现

#### 2.3.1. 问题分析

不同游戏的规则实现中存在重复代码。

#### 2.3.2. 改进建议

使用策略模式封装规则算法：

```java
public interface WinStrategy {
    boolean checkWin(Board board, Position lastMove, Piece piece);
}

public class ConnectFiveWinStrategy implements WinStrategy {
    @Override
    public boolean checkWin(Board board, Position lastMove, Piece piece) {
        // 实现五子连珠的胜利判断
        return checkHorizontal(board, lastMove, piece) ||
               checkVertical(board, lastMove, piece) ||
               checkDiagonal(board, lastMove, piece);
    }
    // ...辅助方法
}
```

## 3. Part3: Lab2到Lab5的转换分析

### 3.1. Part3.1: Lab2到Lab3的转换分析

Lab2到Lab3的转换主要体现在引入了更复杂的游戏规则和多种棋类游戏支持。

#### 3.1.1. 结构变化

Lab2中采用单一棋盘类设计，Lab3引入了抽象层次：

```java
// Lab2: 单一具体棋盘类
public class Board {
    private Piece[][] pieces;
    
    public boolean placePiece(int x, int y, Piece piece) {
        // 简单的落子逻辑
    }
}

// Lab3: 引入抽象类和子类
public abstract class Board {
    protected Piece[][] pieces;
    
    public abstract boolean placePiece(Position pos, Piece piece);
}

public class GoBoard extends Board {
    @Override
    public boolean placePiece(Position pos, Piece piece) {
        // 围棋特定的落子逻辑
    }
}
```

#### 3.1.2. OOP方法应用

- **抽象**：创建了抽象棋盘类，定义通用接口
- **继承**：不同游戏的棋盘继承自抽象棋盘类
- **多态**：通过统一接口处理不同棋类的特定行为

这种改变大大提高了代码的可扩展性，为后续增加新的棋类游戏奠定了基础。

### 3.2. Part3.2: Lab3到Lab4的转换分析

Lab3到Lab4的转换重点是引入了规则引擎和胜负判定机制。

#### 3.2.1. 结构变化

Lab4采用了组合模式，将游戏规则从棋盘类中分离出来：

```java
// Lab3: 规则嵌入在棋盘类中
public class GoBoard extends Board {
    @Override
    public boolean placePiece(Position pos, Piece piece) {
        // 规则验证和落子逻辑混合
        if (!validateMove(pos, piece)) {
            return false;
        }
        // 执行落子
        // 检查胜利条件
    }
    
    private boolean validateMove(Position pos, Piece piece) {
        // 规则验证逻辑
    }
}

// Lab4: 抽取规则到独立类
public class GoRules implements Rules {
    @Override
    public boolean validateMove(Board board, Position pos, Piece piece) {
        // 独立的规则验证逻辑
    }
    
    @Override
    public boolean checkWin(Board board, Position lastMove, Piece piece) {
        // 独立的胜负判定逻辑
    }
}
```

#### 3.2.2. OOP方法应用

- **封装**：将规则逻辑封装到专门的类中
- **组合**：Game类组合Rules对象，而不是继承
- **接口**：通过Rules接口定义规则行为

这种改进降低了系统的耦合度，使得规则可以独立更改而不影响棋盘实现。

### 3.3. Part3.3: Lab4到Lab5的转换分析

Lab4到Lab5的主要变化是增加了游戏历史记录和悔棋功能。

#### 3.3.1. 结构变化

Lab5引入了命令模式和备忘录模式的思想：

```java
// Lab5新增: 移动命令类
public class Move {
    private Position position;
    private Piece piece;
    private Player player;
    
    // 构造函数、getter等
}

// Lab5新增: 游戏历史类
public class GameHistory {
    private Stack<Move> history;
    
    public void recordMove(Move move) {
        history.push(move);
    }
    
    public Move undoLastMove() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }
}
```

#### 3.3.2. OOP方法应用

- **组合**：Game类组合GameHistory类
- **封装**：历史记录操作被封装在GameHistory类中
- **单一职责**：GameHistory类专注于处理历史记录功能

这一改进使得悔棋功能的实现更加清晰，也为可能的重放功能留下了扩展空间。

## 4. Part4: 对图形化界面的影响分析

当前的设计对未来实现图形化界面有以下影响：

### 4.1. 有利因素

1. **模型与视图分离**：核心游戏逻辑独立于界面表示，符合MVC架构思想
2. **清晰的状态管理**：游戏状态集中管理，便于UI组件获取和显示
3. **事件系统**：如果实现了观察者模式，UI可以方便地响应游戏事件

### 4.2. 潜在挑战

1. **界面更新粒度**：当前状态变化没有细分，可能导致UI不必要的完全重绘
2. **UI组件与游戏逻辑的映射**：需要设计合理的适配器连接模型和视图
3. **用户交互处理**：需要将用户界面事件正确转换为游戏命令

### 4.3. 改进建议

为更好地支持图形界面，可以考虑：

1. 引入完整的观察者模式，细化事件类型
2. 实现命令模式处理用户输入
3. 提供游戏状态的只读视图给UI组件

通过这些改进，可以确保图形界面实现时能够与现有代码无缝集成，降低重构成本。
