# OOP

## 1. Chapter 14: JavaFX Basics (JavaFX 基础)

### 1.1. Motivations (动机)

JavaFX 是一个用于开发 Java GUI 程序的新框架。它展示了面向对象原则的优秀应用。本章旨在介绍 JavaFX 编程基础，并演示如何用 JavaFX 实现 OOP。具体而言，本章将介绍 JavaFX 框架以及 GUI 组件及其关系。

### 1.2. Objectives (目标)

本章目标包括：区分 JavaFX、Swing 和 AWT，编写简单的 JavaFX 程序并理解舞台 (stage)、场景 (scene) 和节点 (node) 之间的关系，使用窗格 (panes)、UI 控件和形状 (shapes) 创建用户界面，使用绑定属性 (binding properties) 同步属性值，使用通用属性 `style` 和 `rotate` 进行节点操作，使用 `Color` 类创建颜色，使用 `Font` 类创建字体，使用 `Image` 和 `ImageView` 类创建和显示图像，使用 `Pane`、`StackPane`、`FlowPane`、`GridPane`、`BorderPane`、`HBox` 和 `VBox` 布局节点，使用 `Text` 类显示文本并使用 `Line`、`Circle`、`Rectangle`、`Ellipse`、`Arc`、`Polygon` 和 `Polyline` 创建形状，以及开发可重用 GUI 组件 `ClockPane` 以显示模拟时钟。

### 1.3. JavaFX vs Swing and AWT (JavaFX 与 Swing 和 AWT 对比)

JavaFX 旨在取代 Swing 和 AWT，用于开发丰富的互联网应用程序。

- **AWT (Abstract Windows Toolkit)**：Java 早期 GUI 库，适用于简单 GUI，但易出现平台特定 bug。
- **Swing**：取代 AWT 的更健壮、多功能和灵活的库，组件直接绘制在画布上，对平台依赖性较低。
- **JavaFX**：Java 8 引入的全新 GUI 平台。

### 1.4. 创建 JavaFX 项目 (Creating JavaFX Projects)

可以通过以下方式创建 JavaFX 项目：

- Maven：使用 Maven archetype。
- VSCode 插件：
    1. Extension Pack for Java (microsoft)
    2. Maven for Java
    3. Project Manager for Java
    4. Language Support for Java(TM) by Red Hat

### 1.5. Basic Structure of JavaFX (JavaFX 基本结构)

JavaFX 应用程序的基本结构包括：

- **Application (应用程序)**：JavaFX 程序的入口点。
- **Override the `start(Stage)` method (重写 `start(Stage)` 方法)**：应用程序的主入口点。
- **Stage (舞台)**：顶级容器，代表窗口。
- **Scene (场景)**：舞台中的内容容器，包含所有 UI 节点。
- **Nodes (节点)**：场景中的图形元素（如按钮、文本等）。

### 1.6. Panes, UI Controls, and Shapes (窗格、UI 控件和形状)

- **节点 (Node)**：所有可见元素（包括 UI 控件、形状等）的基类。
- **父节点 (Parent)**：可以包含其他节点的节点，如 `Pane` 和 `Control`。
- **窗格 (Pane)**：用于布局节点的容器，包括 `FlowPane`、`GridPane`、`BorderPane`、`HBox`、`VBox`、`StackPane`。
- **UI 控件 (Control)**：用户界面交互元素，如 `Label`、`TextField`、`Button`、`CheckBox`、`RadioButton`、`TextArea`。
- **形状 (Shape)**：图形元素，如 `Line`、`Circle`、`Ellipse`、`Rectangle`、`Path`、`Polygon`、`Polyline`、`Text`。
- **ImageView**：用于显示图像。

### 1.7. Display a Shape (显示形状)

JavaFX 使用传统的数学坐标系，y 轴向下。

### 1.8. Binding Properties (绑定属性)

JavaFX 引入了**绑定属性**的概念。它允许一个目标对象（或其属性）与一个源对象（或其属性）绑定。当源对象的值发生变化时，目标属性会自动更新。目标对象被称为**绑定对象**或**绑定属性**。

### 1.9. Uni/Bidirectional Binding (单向/双向绑定)

- **单向绑定**：源属性的变化影响目标属性，但目标属性的变化不影响源属性。
- **双向绑定**：源属性和目标属性相互影响，任何一方的变化都会同步到另一方。

### 1.10. Common Properties and Methods for Nodes (节点的通用属性和方法)

- **`style`**: 用于设置 JavaFX CSS 样式。
- **`rotate`**: 用于旋转节点。

### 1.11. The Color Class (Color 类)

`javafx.scene.paint.Color` 类用于表示颜色。

- **属性**：`red`、`green`、`blue` (0.0-1.0 范围) 和 `opacity` (透明度，0.0-1.0 范围)。
- **构造函数**：根据 RGB 和透明度值创建颜色。
- **方法**：`brighter()` (更亮)、`darker()` (更暗)，以及获取不透明或 0-255 RGB 值的颜色。

### 1.12. The Font Class (Font 类)

`javafx.scene.text.Font` 类用于管理字体。

- **属性**：`size`、`name` 和 `family`。
- **构造函数**：可指定字体名称、大小、粗细、姿态等。
- **方法**：`getFamilies()` (获取字体家族列表)、`getFontNames()` (获取完整字体名称列表)。

### 1.13. The Image Class (Image 类)

`javafx.scene.image.Image` 类用于加载图像。

- **属性**：`error` (加载是否正确)、`height`、`width` (图像高度和宽度)、`progress` (加载进度)。
- **构造函数**：通过文件名或 URL 创建 `Image` 对象。

### 1.14. The ImageView Class (ImageView 类)

`javafx.scene.image.ImageView` 类是 `Node` 的子类，用于显示 `Image` 对象。

- **属性**：`fitHeight`、`fitWidth` (适应高度和宽度)、`x`、`y` (图像视图原点坐标)、`image` (要显示的图像)。
- **构造函数**：创建空的 `ImageView`、或带指定图像的 `ImageView`、或从文件/URL 加载图像的 `ImageView`。

### 1.15. Layout Panes (布局窗格)

JavaFX 提供多种窗格来组织容器中的节点。

- **`Pane`**：布局窗格的基类，包含 `getChildren()` 方法。
- **`StackPane`**：节点堆叠在彼此上方，居中显示。
- **`FlowPane`**：节点按行或列垂直/水平排列。
- **`GridPane`**：节点放置在二维网格的单元格中。
- **`BorderPane`**：节点放置在顶部、右侧、底部、左侧和中心区域。
- **`HBox`**：节点在一行中放置。
- **`VBox`**：节点在一列中放置。

### 1.16. FlowPane (流式窗格)

`FlowPane` 用于按行或列布局节点。

- **对齐方式 (`alignment`)**: 默认 `Pos.LEFT`。
- **方向 (`orientation`)**: 默认 `Orientation.HORIZONTAL`。
- **水平间距 (`hgap`)** 和 **垂直间距 (`vgap`)**。

### 1.17. GridPane (网格窗格)

`GridPane` 用于将节点放置在二维网格的单元格中。

- **对齐方式 (`alignment`)**：默认 `Pos.LEFT`。
- **网格线可见性 (`gridLinesVisible`)**：默认 `false`。
- **水平间距 (`hgap`)** 和 **垂直间距 (`vgap`)**。
- **添加节点 (`add`)**: 指定子节点、列索引和行索引。

### 1.18. BorderPane (边框窗格)

`BorderPane` 用于将节点放置在顶部、右侧、底部、左侧和中心区域。

- 每个区域只能放置一个节点。
- 提供了设置每个区域节点和对齐方式的方法。

### 1.19. HBox (水平箱式窗格)

`HBox` 用于将节点排列在单行中。

- **对齐方式 (`alignment`)**：默认 `Pos.TOP_LEFT`。
- **填充高度 (`fillHeight`)**：默认 `true`，可调整子节点填充高度。
- **间距 (`spacing`)**：子节点之间的水平间距。

### 1.20. VBox (垂直箱式窗格)

`VBox` 用于将节点排列在单列中。

- **对齐方式 (`alignment`)**：默认 `Pos.TOP_LEFT`。
- **填充宽度 (`fillWidth`)**：默认 `true`，可调整子节点填充宽度。
- **间距 (`spacing`)**：子节点之间的垂直间距。

### 1.21. Shapes (形状)

JavaFX 提供多种形状类，用于绘制文本、线条、圆形、矩形、椭圆、弧、多边形和多段线。这些形状类都继承自 `Node`。

### 1.22. Text (文本)

`javafx.scene.text.Text` 类用于显示文本。

- **属性**：`text` (要显示的文本)、`x`、`y` (文本坐标)、`underline` (下划线)、`strikethrough` (删除线)、`font` (字体)。
- **构造函数**：创建空文本、或带指定文本的文本、或带指定坐标和文本的文本。

### 1.23. Line (线条)

`javafx.scene.shape.Line` 类用于绘制线条。

- **属性**：`startX`、`startY` (起点坐标)、`endX`、`endY` (终点坐标)。
- **构造函数**：创建空线条或带指定起终点坐标的线条。

### 1.24. Rectangle (矩形)

`javafx.scene.shape.Rectangle` 类用于绘制矩形。

- **属性**：`x`、`y` (左上角坐标)、`width`、`height` (宽度和高度)、`arcWidth`、`arcHeight` (圆弧宽度和高度，用于圆角矩形)。
- **构造函数**：创建空矩形或带指定属性的矩形。

### 1.25. Circle (圆形)

`javafx.scene.shape.Circle` 类用于绘制圆形。

- **属性**：`centerX`、`centerY` (圆心坐标)、`radius` (半径)。
- **构造函数**：创建空圆形或带指定圆心和半径的圆形。

### 1.26. Ellipse (椭圆)

`javafx.scene.shape.Ellipse` 类用于绘制椭圆。

- **属性**：`centerX`、`centerY` (圆心坐标)、`radiusX`、`radiusY` (水平和垂直半径)。
- **构造函数**：创建空椭圆或带指定属性的椭圆。

### 1.27. Arc (弧)

`javafx.scene.shape.Arc` 类用于绘制弧。

- **属性**：`centerX`、`centerY` (圆心坐标)、`radiusX`、`radiusY` (水平和垂直半径)、`startAngle` (起始角度)、`length` (弧长)、`type` (闭合类型，如 `OPEN`, `CHORD`, `ROUND`)。
- **构造函数**：创建空弧或带指定属性的弧。

### 1.28. Polygon and Polyline (多边形和多段线)

- **Polygon (多边形)**：由一系列连接点组成的闭合形状。
- **Polyline (多段线)**：由一系列连接点组成的非闭合形状。
- 两者都通过 `getPoints()` 返回一个 `ObservableList<Double>`，存储点的 x 和 y 坐标。

### 1.29. Case Study: The ClockPane Class (案例研究：时钟窗格类)

`ClockPane` 类是一个自定义窗格，用于显示模拟时钟。

- **属性**：`hour`、`minute`、`second` (时、分、秒)。
- **方法**：`ClockPane()` (构造函数)、`setCurrentTime()` (设置当前时间)、`setWidth()`、`setHeightTime()` (设置宽度和高度并重绘时钟)。

## 2. Chapter 15: Event-Driven Programming and Animations (事件驱动编程和动画)

### 2.1. Motivations (动机)

开发 GUI 程序需要处理用户交互。**事件驱动编程**是一种响应用户操作（如点击按钮）的编程范式。

### 2.2. Objectives (目标)

本章目标包括：理解事件驱动编程，描述事件、事件源和事件类，定义处理类、注册处理对象和编写事件处理代码，使用内部类和匿名内部类定义处理类，使用 Lambda 表达式简化事件处理，开发贷款计算器 GUI，处理鼠标事件 (`MouseEvent`) 和键盘事件 (`KeyEvent`)，创建监听器处理可观察对象的值变化，使用 `Animation`、`PathTransition`、`FadeTransition` 和 `Timeline` 类开发动画，以及模拟弹跳球动画。

### 2.3. Procedural vs. Event-Driven Programming (过程式 vs. 事件驱动编程)

- **过程式编程**：代码按程序顺序执行。
- **事件驱动编程**：代码在事件激活时执行。

### 2.4. Taste of Event-Driven Programming (事件驱动编程初体验)

一个简单的 GUI 示例，显示一个按钮。当按钮被点击时，控制台会显示消息，演示事件响应。

### 2.5. Handling GUI Events (处理 GUI 事件)

- **事件源对象 (Source object)**：生成事件的对象（例如，按钮）。
- **监听器对象 (Listener object)**：包含处理事件的方法。
- **事件**：由事件源对象触发。
- **处理程序 (Handler)**：监听器对象中的方法，实际处理事件。

### 2.6. Trace Execution (追踪执行)

一个 JavaFX 事件处理的追踪示例：

1. 从 `main` 方法开始，创建 GUI 窗口。
2. 点击“OK”按钮。
3. JVM 调用监听器的 `handle` 方法。
4. `handle` 方法中的代码执行，例如在控制台打印消息。

### 2.7. Events (事件)

- 事件可以定义为程序收到的一种信号，表明发生了某事。
- 事件通常由外部用户操作生成，例如鼠标移动、鼠标点击或键盘按键。

### 2.8. Event Classes (事件类)

JavaFX 事件类位于 `javafx.event` 包中。
事件类层次结构：

- `EventObject` (根) -> `Event`
    - `ActionEvent` (按钮点击等)
    - `InputEvent` (输入事件)
        - `MouseEvent` (鼠标事件)
        - `KeyEvent` (键盘事件)
    - `WindowEvent` (窗口事件)

### 2.9. Event Information (事件信息)

- 事件对象包含与事件相关的所有属性。
- 可以使用 `EventObject` 类中的 `getSource()` 方法识别事件源对象。
- `EventObject` 的子类处理特殊类型的事件，如按钮动作、窗口事件、鼠标移动和按键。

### 2.10. Selected User Actions and Handlers (选定的用户操作和处理程序)

表格列出了常见用户操作、事件源对象、事件类型以及相应的事件注册方法。
例如，按钮点击触发 `ActionEvent`，注册方法为 `setOnAction(EventHandler<ActionEvent>)`。鼠标事件和键盘事件则有更细分的类型和注册方法。

### 2.11. The Delegation Model (委托模型)

- **事件源**：生成事件并注册监听器。
- **监听器**：实现事件处理接口，包含处理事件的 `handle` 方法。
- **注册**：事件源将监听器对象注册到自身，以便在事件发生时调用 `handle` 方法。

### 2.12. The Delegation Model: Example (委托模型：示例)

示例代码展示了如何创建按钮 (`Button`)、处理类 (`OKHandlerClass`) 和事件注册 (`setOnAction`)。

```java
Button btOK = new Button("OK"); //
OKHandlerClass handler = new OKHandlerClass(); //
btOK.setOnAction(handler); // 注册事件处理
```

### 2.13. Example: First Version for ControlCircle (no listeners) (示例：ControlCircle 的第一个版本（无监听器）)

演示一个包含两个按钮（放大、缩小）控制圆形大小的程序，但尚未添加监听器，因此按钮无效。

### 2.14. Example: Second Version for ControlCircle (with listener for Enlarge) (示例：ControlCircle 的第二个版本（带放大监听器）)

在第一个版本的基础上，为“放大”按钮添加了监听器，使其能够响应点击事件并改变圆形大小。

### 2.15. Inner Class Listeners (内部类监听器)

- 监听器类通常只用于特定 GUI 组件，不共享给其他应用程序。
- 适合将监听器类定义为框架类内部的**内部类 (inner class)**。

### 2.16. Inner Classes (内部类)

- **定义**：一个类是另一个类的成员。
- **优势**：
    - 使程序更简单。
    - 内部类可以访问其包含的外部类的数据和方法，无需传递外部类引用给内部类构造函数。

### 2.17. Inner Classes, cont. (内部类，续)

内部类使得程序更简洁。
内部类被编译成 `OuterClassName$InnerClassName.class` 的形式。
内部类可以声明为 `public`、`protected` 或 `private`，遵循与成员相同的可见性规则。
静态内部类可以不依赖外部类实例而访问，但不能访问外部类的非静态成员。

### 2.18. Anonymous Inner Classes (匿名内部类)

- **定义**：没有名称的内部类，通常直接在创建实例时声明。
- **特性**：
    - 必须扩展一个超类或实现一个接口，但不能有显式的 `extends` 或 `implements` 子句。
    - 必须实现超类或接口中的所有抽象方法。
    - 总是使用其超类的无参构造函数创建实例（如果实现接口，则使用 `Object()` 的无参构造函数）。
    - 编译后名为 `OuterClassName$n.class`，其中 `n` 是数字。

### 2.19. Simplifying Event Handling Using Lambda Expressions (使用 Lambda 表达式简化事件处理)

Lambda 表达式是 Java 8 的新特性，可将匿名方法简化为更简洁的语法。它们是**单抽象方法接口 (SAM)** 的实现，即接口中只有一个抽象方法。

### 2.20. Basic Syntax for a Lambda Expression (Lambda 表达式基本语法)

- `(type1 param1, type2 param2, ...) -> expression`
- `(type1 param1, type2 param2, ...) -> { statements; }`
- 参数的数据类型可以显式声明或隐式推断。如果只有一个参数且数据类型可推断，则括号可以省略。

### 2.21. The `MouseEvent` Class (MouseEvent 类)

`javafx.scene.input.MouseEvent` 类用于处理鼠标事件。

- **信息**：按钮（左/中/右）、点击次数、`x/y` 坐标（事件源/场景/屏幕）、是否按下了 `Alt/Control/Meta/Shift` 键。

### 2.22. The `KeyEvent` Class (KeyEvent 类)

`javafx.scene.input.KeyEvent` 类用于处理键盘事件。

- **信息**：字符 (`getCharacter()`)、键码 (`getCode()`)、文本 (`getText()`)、是否按下了 `Alt/Control/Meta/Shift` 键。

### 2.23. The KeyCode Constants (KeyCode 常量)

`KeyCode` 类定义了键盘按键的常量，例如 `HOME`, `END`, `PAGE_UP`, `ENTER`, `F1` 到 `F12`, `0` 到 `9`, `A` 到 `Z` 等。

### 2.24. Listeners for Observable Objects (可观察对象的监听器)

- 可以为**可观察对象 (Observable object)** 添加监听器，以处理其值的变化。
- 可观察对象具有 `addListener(InvalidationListener listener)` 方法。
- 监听器类应实现 `InvalidationListener` 接口，并使用 `invalidated(Observable o)` 方法处理属性值变化。

### 2.25. Animation (动画)

JavaFX 提供了 `Animation` 类作为所有动画的核心功能。

- **属性**：`autoReverse` (是否反向播放)、`cycleCount` (循环次数)、`rate` (播放速度)、`status` (动画状态)。
- **方法**：`pause()` (暂停)、`play()` (播放)、`stop()` (停止并重置)。

### 2.26. PathTransition (路径过渡)

`PathTransition` 类动画化一个节点沿着给定路径移动。

- **属性**：`duration` (持续时间)、`node` (目标节点)、`orientation` (节点方向)、`path` (形状路径)。
- **构造函数**：可指定持续时间、路径和节点。

### 2.27. FadeTransition (淡入淡出过渡)

`FadeTransition` 类动画化节点透明度的变化。

- **属性**：`duration` (持续时间)、`node` (目标节点)、`fromValue` (起始透明度)、`toValue` (结束透明度)、`byValue` (透明度增量)。
- **构造函数**：可指定持续时间、节点和透明度值。

### 2.28. Timeline (时间轴)

`Timeline` 类用于程序化地创建动画，通过一个或多个 `KeyFrame` 来实现。每个 `KeyFrame` 在指定的时间间隔内顺序执行。`Timeline` 继承自 `Animation`。

### 2.29. Case Study: Bouncing Ball (案例研究：弹跳球)

通过 `BallPane` 类表示一个弹跳球，并通过 `BounceBallControl` 类控制其运动，可以使用滑块控制速度。

## 3. Chapter 16: JavaFX UI Controls and Multimedia (JavaFX UI 控件和多媒体)

### 3.1. Motivations (动机)

图形用户界面 (GUI) 使系统用户友好且易于使用。创建 GUI 需要创造力和 GUI 组件的工作原理知识。Java 中的 GUI 组件非常灵活和多功能，可以创建各种有用的用户界面。

### 3.2. Objectives (目标)

本章目标包括：使用各种 UI 控件创建 GUI，使用 `Label` 及其属性，使用 `Button` 及其 `setOnAction`，使用 `CheckBox` 和 `RadioButton`（配合 `ToggleGroup`），使用 `TextField` 和 `PasswordField` 输入数据，使用 `TextArea` 输入多行数据，使用 `ComboBox` 单选，使用 `ListView` 单选/多选，使用 `ScrollBar` 和 `Slider` 选择范围，开发 Tic-Tac-Toe 游戏，使用 `Media`、`MediaPlayer` 和 `MediaView` 播放视频和音频，以及开发显示国旗和播放国歌的案例研究。

### 3.3. Frequently Used UI Controls (常用 UI 控件)

JavaFX 中常用的 UI 控件包括：`Label`, `Button`, `CheckBox`, `RadioButton`, `TextField`, `PasswordField`, `TextArea`, `ComboBox`, `ListView`, `ScrollBar`, `Slider`。这些控件通常继承自 `Control` 或 `Labeled`。

### 3.4. Labeled (带标签的控件)

`Labeled` 类是显示短文本、节点或两者皆有的显示区域的基类。`Label` 和 `Button` 等控件共享 `Labeled` 中定义的许多通用属性。

- **属性**：`alignment` (对齐方式)、`contentDisplay` (内容显示方式)、`graphic` (图形)、`graphicTextGap` (图形与文本间距)、`textFill` (文本颜色)、`text` (文本内容)、`underline` (下划线)、`wrapText` (文本换行)。

### 3.5. Label (标签)

`Label` 类定义了标签。

- **构造函数**：创建空标签、带文本的标签、或带文本和图形的标签。

### 3.6. ButtonBase and Button (按钮基类和按钮)

- **`ButtonBase`**：是触发动作事件的控件的基类。
- **`Button`**：触发点击事件的按钮。
- **共同特性**：`onAction` (动作事件处理器)。
- **构造函数**：创建空按钮，或带文本的按钮，或带文本和图形的按钮。

### 3.7. CheckBox (复选框)

`CheckBox` 用于用户进行选择。它继承了 `ButtonBase` 和 `Labeled` 的所有属性（如 `onAction`、`text`、`graphic`、`alignment` 等）。

- **属性**：`selected` (是否选中)。
- **构造函数**：创建空复选框或带文本的复选框。

### 3.8. RadioButton (单选按钮)

`RadioButton` 允许用户从一组选项中选择单个项目。它继承了 `ToggleButton` 的所有属性。

- **外观**：通常显示为一个圆形，选中时填充。
- **属性**：`selected` (是否选中)、`toggleGroup` (所属的单选组)。

### 3.9. TextField (文本字段)

`TextField` 用于输入或显示单行字符串。它是 `TextInputControl` 的子类。

- **属性**：`text` (文本内容)、`editable` (是否可编辑)、`alignment` (文本对齐)、`prefColumnCount` (首选列数)、`onAction` (动作事件处理器)。
- **构造函数**：创建空文本字段或带初始文本的文本字段。

### 3.10. TextArea (文本区域)

`TextArea` 允许用户输入多行文本。

- **属性**：`prefColumnCount` (首选列数)、`prefRowCount` (首选行数)、`wrapText` (是否换行)。
- **构造函数**：创建空文本区域或带初始文本的文本区域。

### 3.11. ComboBox (组合框)

`ComboBox` 允许用户从一个列表中选择一个项目，也称为选择列表或下拉列表。

- **属性**：`value` (选中的值)、`editable` (是否可编辑)、`onAction` (动作事件处理器)、`items` (列表项)、`visibleRowCount` (可见行数)。
- **构造函数**：创建空组合框或带指定列表项的组合框。

### 3.12. ListView (列表视图)

`ListView` 类似于 `ComboBox`，但它允许用户选择单个或多个值。

- **属性**：`items` (列表项)、`orientation` (方向，水平/垂直)、`selectionModel` (选择模型)。
- **构造函数**：创建空列表视图或带指定列表项的列表视图。

### 3.13. ScrollBar (滚动条)

`ScrollBar` 允许用户从一个范围中选择一个值。滚动条有两种样式：水平和垂直。

- **属性**：`blockIncrement` (块增量)、`max` (最大值)、`min` (最小值)、`unitIncrement` (单位增量)、`value` (当前值)、`visibleAmount` (可见部分大小)、`orientation` (方向)。

### 3.14. Slider (滑块)

`Slider` 类似于 `ScrollBar`，但具有更多属性，并可以以多种形式出现。

- **属性**：与 `ScrollBar` 类似，并增加了 `majorTickUnit` (主刻度单位)、`minorTickCount` (次刻度数量)、`showTickLabels` (显示刻度标签)、`showTickMarks` (显示刻度标记)。

### 3.15. Case Study: TicTacToe (案例研究：井字棋)

井字棋游戏示例，包含 `Cell` 类表示棋盘上的单元格，以及 `TicTacToe` 类管理游戏逻辑和 UI。

### 3.16. Media (媒体)

- `Media` 类用于获取媒体源。
- `MediaPlayer` 类用于播放和控制媒体（如 `autoPlay`, `currentCount`, `cycleCount`, `mute`, `volume`, `totalDuration` 等属性）。
- `MediaView` 类是 `Node` 的子类，用于显示视频。
