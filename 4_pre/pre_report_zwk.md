# 预评审报告 zwk

---

- [1. Part1 Lab5代码分析](#1-part1-lab5代码分析)
    - [1.1. 整体架构分析](#11-整体架构分析)
    - [1.2. 关键代码分析](#12-关键代码分析)
    - [1.3. 亮点分析](#13-亮点分析)
- [2. Part2 Lab5的改进建议](#2-part2-lab5的改进建议)
    - [2.1. 增强输入解析器的灵活性和容错性](#21-增强输入解析器的灵活性和容错性)
    - [2.2. 优化游戏模式间的共享代码](#22-优化游戏模式间的共享代码)
    - [2.3. 引入更清晰的胜负判定接口](#23-引入更清晰的胜负判定接口)
    - [2.4. 简化类型转换操作](#24-简化类型转换操作)
- [3. Part3 Lab2到Lab5的演进分析](#3-part3-lab2到lab5的演进分析)
    - [3.1. Part3.1 lab2到lab3的转换分析](#31-part31-lab2到lab3的转换分析)
        - [3.1.1. 主要变化](#311-主要变化)
        - [3.1.2. 关键代码对比](#312-关键代码对比)
    - [3.2. Part3.2 lab3到lab4的转换分析](#32-part32-lab3到lab4的转换分析)
        - [3.2.1. 主要变化](#321-主要变化)
        - [3.2.2. 关键改进](#322-关键改进)
    - [3.3. Part3.3 lab4到lab5的转换分析](#33-part33-lab4到lab5的转换分析)
        - [3.3.1. 主要变化](#331-主要变化)
        - [3.3.2. 代码分析](#332-代码分析)
- [4. Part4 设计对图形界面的影响](#4-part4-设计对图形界面的影响)
    - [4.1. 正面影响](#41-正面影响)
    - [4.2. 需要适应的部分](#42-需要适应的部分)
    - [4.3. 具体适应建议](#43-具体适应建议)

---

## 1. Part1 Lab5代码分析

### 1.1. 整体架构分析

Lab5在Lab4的基础上，通过增量式开发添加了五子棋(gomoku)模式。整体架构保持了良好的扩展性，主要结构如下：

1. **顶层容器结构**
   - `GameContainer`：游戏主容器，负责管理游戏流程
   - `GameList`：管理多个游戏实例

2. **游戏模式层次结构**
   - `GameMode`：抽象基类，定义游戏共有的属性和行为
   - `PeaceMode`：和平模式实现
   - `ReversiMode`：翻转棋模式实现
   - `GomokuMode`：五子棋模式实现（Lab5新增）

3. **命令处理系统**
   - `InputParser`：解析用户输入为`InputResult`对象
   - `CommandFactory`：根据`InputResult`创建具体命令
   - 各种具体命令类实现不同操作

### 1.2. 关键代码分析

1. **GomokuMode实现**：

    ```java
    public class GomokuMode extends GameMode
    {
        private Player winner;

        @Override
        public boolean receiveOperation(Point point) 
        {
            return go(point);
        }

        @Override
        public boolean receiveOperation(String operation)
        {
            switch(operation) {
                case "quit":
                    return true;
                default:
                    return false;
            }
        }
        
        private boolean checkLink(Point point)
        {
            EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
            int[] linkedNumber = {1, 1, 1, 1};
            for(Direction direction : directions)
            {
                Point focus = new Point(point);
                while(moveFocus(focus, direction))
                {
                    if(getChessColor(focus) == (isBlackTurn ? ChessColor.BLACK : ChessColor.WHITE))
                    {
                        linkedNumber[direction.ordinal() % 4]++;
                    }
                    else
                    {
                        break;
                    }
                }

                if(linkedNumber[direction.ordinal() % 4] >= 5)
                {
                    return true;
                }
            }
            return false;
        }
    }
    ```

2. **InputParser更新**：

    ```java
    // 命令格式: quit, pass, peace, reversi, gomoku
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(quit|pass|peace|reversi|gomoku)$", Pattern.CASE_INSENSITIVE);

    // 检查是否是命令
    Matcher commandMatcher = COMMAND_PATTERN.matcher(input);
    if (commandMatcher.matches()) 
    {
        String command = input.toLowerCase();
        switch (command) 
        {
            case "quit":
                return InputResult.quit();
            case "pass":
                return InputResult.pass();
            case "peace":
            case "reversi":
            case "gomoku": // 新增五子棋模式
                return InputResult.newGame(command);
        }
    }
    ```

3. **PrintTools提示信息更新**：

    ```java
    switch(currentGame.getGameMode())
    {
        case "peace":
            tips += "3. 增加棋盘(peace/reversi/gomoku)\n 4. 退出游戏(quit)";
            break;
        case "reversi":
            tips += "3. 跳过(pass)\n 4. 增加棋盘(peace/reversi/gomoku)\n 5. 退出游戏(quit)";
            break;
        case "gomoku":
            tips += "3. 增加棋盘(peace/reversi/gomoku)\n 4. 退出游戏(quit)";
            break;
    }
    ```

### 1.3. 亮点分析

1. **高度的可扩展性**：新增五子棋模式只需实现`GameMode`的抽象方法，无需修改核心架构。

2. **多态的有效应用**：各个游戏模式通过实现抽象类方法体现多态，如：
   - `receiveOperation(Point point)`：不同游戏模式有不同的落子规则
   - `receiveOperation(String operation)`：不同游戏模式处理不同的命令

3. **模块化设计**：游戏逻辑、输入处理和界面显示分离，降低了耦合度。

4. **命令模式的灵活应用**：通过命令模式处理用户输入，使得新增操作非常简便。

## 2. Part2 Lab5的改进建议

尽管Lab5已经具有良好的设计，但仍有一些可改进的地方：

### 2.1. 增强输入解析器的灵活性和容错性

当前的`InputParser`使用固定的正则表达式进行匹配，可以进一步增强其灵活性：

```java
// 当前实现
private static final Pattern COMMAND_PATTERN = Pattern.compile("^(quit|pass|peace|reversi|gomoku)$", Pattern.CASE_INSENSITIVE);

// 改进建议：使用更灵活的命令注册机制
public class InputParser {
    private static Map<String, CommandType> registeredCommands = new HashMap<>();
    
    public static void registerCommand(String command, CommandType type) {
        registeredCommands.put(command.toLowerCase(), type);
    }
    
    public static InputResult parseCommand(String input) {
        String lowercaseInput = input.toLowerCase().trim();
        CommandType type = registeredCommands.get(lowercaseInput);
        if (type != null) {
            // 处理命令
        }
        // 其他解析逻辑
    }
}
```

这样新增命令只需注册，不需修改正则表达式和switch语句。

### 2.2. 优化游戏模式间的共享代码

在`ReversiMode`和`GomokuMode`中，有一些类似的代码，如`moveFocus`方法：

```java
// ReversiMode.java
private boolean moveFocus(Point focus, Direction direction)
{
    int dx = Direction.getDirectionDelta(direction).x;
    int dy = Direction.getDirectionDelta(direction).y;

    if(focus.x + dx >= 0 && focus.x + dx < maxSize &&
       focus.y + dy >= 0 && focus.y + dy < maxSize)
    {
        focus.x += dx;
        focus.y += dy;
        return true;
    }
    else return false;
}

// GomokuMode.java 中有几乎相同的代码
```

建议将这些共享方法提到`GameMode`基类中：

```java
// 建议在GameMode中添加
protected boolean moveFocus(Point focus, Direction direction)
{
    int dx = Direction.getDirectionDelta(direction).x;
    int dy = Direction.getDirectionDelta(direction).y;

    if(focus.x + dx >= 0 && focus.x + dx < maxSize &&
       focus.y + dy >= 0 && focus.y + dy < maxSize)
    {
        focus.x += dx;
        focus.y += dy;
        return true;
    }
    else return false;
}
```

### 2.3. 引入更清晰的胜负判定接口

当前，不同模式有不同的胜负判断方式，建议统一接口：

```java
// 在GameMode中添加
public abstract Player getWinner();
public abstract boolean isGameOver();

// 这样可以在GameContainer中统一处理胜负逻辑
if (currentGame.isGameOver()) {
    Player winner = currentGame.getWinner();
    // 处理游戏结束逻辑
}
```

### 2.4. 简化类型转换操作

当前代码中需要经常进行向下转型才能访问特定模式的方法：

```java
if(currentGame.getGameMode().equals("reversi")) {
    shouldPass = ((ReversiMode)currentGame).shouldPass();
}
```

建议使用接口或抽象方法解决这个问题：

```java
// 在GameMode中添加
public boolean shouldPass() {
    return false; // 默认实现
}

// ReversiMode覆盖此方法
@Override
public boolean shouldPass() {
    return shouldPass;
}

// 调用时无需强制转换
if(currentGame.shouldPass()) {
    // 处理
}
```

## 3. Part3 Lab2到Lab5的演进分析

### 3.1. Part3.1 lab2到lab3的转换分析

Lab2到Lab3的转换主要体现了**组合**和**封装**的设计原则。

#### 3.1.1. 主要变化

1. **多棋盘支持**：从单一棋盘到支持多棋盘切换

2. **代码结构拆分**：

   - Lab2中：输入处理、游戏逻辑、输出展示都集中在`ReversiGame`类中
   - Lab3中：将输入处理抽离到`ReceiveTools`类，更好地实现了关注点分离

#### 3.1.2. 关键代码对比

**Lab2中的集中式设计：**

```java
// 在ChessBoard类中含有输入输出相关代码
public void initChessBoard()
{
    // ...初始化棋盘
    p1 = new Player();
    p1.initPlayer();
    p2 = new Player();
    p2.initPlayer();
    // ...
}
```

**Lab3中的组合式设计：**

```java
// Lab3中将棋盘初始化改为构造函数方式，由外部提供参数
ChessBoard(String player1Name, String player2Name, ChessColor player1ChessColor, ChessColor player2ChessColor, int num)
{
    boardNum = num;
    chessBoard = new ChessColor[8][8];
    // ...
    p1 = new Player(player1Name, player1ChessColor);
    p2 = new Player(player2Name, player2ChessColor);
}
```

这个转换突显了以下OOP优势：  

- **封装**：将各功能模块化，隐藏内部实现
- **组合**：通过组合不同对象构建复杂系统
- **单一责任原则**：每个类只负责一个功能

### 3.2. Part3.2 lab3到lab4的转换分析

Lab3到Lab4是最大的转变，体现了**抽象**、**继承**和**多态**的深度应用。

#### 3.2.1. 主要变化

1. **架构全面重构**：使用更合理的层次结构设计

2. **命令模式引入**：处理用户输入采用命令模式

3. **抽象基类设计**：引入`GameMode`抽象类

#### 3.2.2. 关键改进

**使用抽象基类和继承实现游戏模式扩展：**

```java
// 抽象基类定义接口
public abstract class GameMode {
    // ...
    public abstract boolean receiveOperation(Point point);
    public abstract boolean receiveOperation(String operation);
}

// 具体实现类
public class ReversiMode extends GameMode {
    @Override
    public boolean receiveOperation(Point point) {
        // 翻转棋特有实现
    }
    
    @Override
    public boolean receiveOperation(String operation) {
        // 翻转棋特有命令处理
    }
}

public class PeaceMode extends GameMode {
    @Override
    public boolean receiveOperation(Point point) {
        // 和平模式特有实现  
    }
}
```

**命令模式的应用：**

```java
public interface GameCommand {
    CommandResult execute(GameMode currentGame, GameList gameList);
}

public class GoCommand implements GameCommand {
    private Point position;
    
    public GoCommand(Point position) {
        this.position = position;
    }
    
    @Override
    public CommandResult execute(GameMode currentGame, GameList gameList) {
        boolean result = currentGame.receiveOperation(position);
        // ...返回命令结果
    }
}
```

这次转换展现了面向对象设计的核心优势：  

- **抽象**：提取共性形成抽象基类
- **继承**：子类继承基类并拓展功能
- **多态**：同一个方法在不同子类有不同实现
- **设计模式**：应用命令模式解耦请求和执行

### 3.3. Part3.3 lab4到lab5的转换分析

Lab4到Lab5的转换展示了良好架构的可扩展性。

#### 3.3.1. 主要变化

1. **新增五子棋模式**：在原有架构基础上添加新游戏模式

2. **功能优化**：细化错误提示，优化显示布局

#### 3.3.2. 代码分析

**通过继承轻松添加新游戏模式：**

```java
public class GomokuMode extends GameMode {
    private Player winner;
    
    public GomokuMode(int order, String mode, int size, String name1, String name2, ChessColor color1, ChessColor color2) {
        super(order, mode, size, name1, name2, color1, color2);
        winner = null;
    }
    
    @Override
    public boolean receiveOperation(Point point) {
        return go(point);
    }
    
    // 五子棋特有的胜利判断逻辑
    private boolean checkLink(Point point) {
        // 实现五子连线检测
    }
}
```

**仅需小幅修改支持新模式：**

```java
// 在InputParser中添加对新模式的支持
private static final Pattern COMMAND_PATTERN = Pattern.compile("^(quit|pass|peace|reversi|gomoku)$", Pattern.CASE_INSENSITIVE);

// 在命令工厂中处理新模式
switch (command) {
    // ...
    case "gomoku":
        return InputResult.newGame(command);
}
```

Lab4到Lab5的转换展示了良好架构带来的好处：  

- **可扩展性**：无需修改核心架构即可添加新功能
- **代码重用**：新模式复用了大量现有代码
- **开闭原则**：对扩展开放，对修改关闭

## 4. Part4 设计对图形界面的影响

当前设计对未来实现图形界面有以下几方面的影响：

### 4.1. 正面影响

1. **逻辑与显示分离**：当前的设计已经将游戏逻辑与显示输出分离，这为图形界面替换控制台输出提供了便利。可以直接替换`PrintTools`，而不影响游戏逻辑。

2. **命令模式的应用**：当前使用的命令模式非常适合处理GUI中的事件。可以轻松将命令绑定到按钮点击、菜单选择等GUI事件上。

3. **模块化的状态管理**：游戏状态的管理比较清晰，可以方便地与图形界面的状态显示绑定。

### 4.2. 需要适应的部分

1. **输入处理转换**：当前的`InputParser`是针对文本输入设计的，需要重新设计为处理鼠标点击和按钮事件的方式。

2. **异步处理需求**：GUI通常需要处理异步事件，当前同步的处理模式需要进行调整。

3. **观察者模式引入**：图形界面通常使用观察者模式更新视图，当前设计可能需要添加事件通知机制。

### 4.3. 具体适应建议

1. 引入MVC模式：
   - Model: 当前的GameMode作为模型层
   - View: 新增的图形界面组件
   - Controller: 负责连接Model和View的控制器

2. 事件系统：
   - 为游戏状态变化定义事件
   - View注册为这些事件的监听器

3. 适配器模式：
   - 创建适配器将GUI事件转换为当前的命令模式可以处理的形式

总体而言，当前设计具有良好的可扩展性和模块化，这将使向图形界面的转换变得相对容易，但仍需要一些架构调整以适应GUI的特殊需求。
