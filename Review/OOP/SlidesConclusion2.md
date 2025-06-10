# OOP

## 1. Chapter 12: Exception Handling and Text IO (异常处理和文本I/O)

### 1.1. Motivations (动机)

当程序运行时遇到运行时错误，程序会异常终止。如何处理运行时错误，使程序能够继续运行或优雅地终止？这就是本章将要介绍的主题。

### 1.2. Objectives (目标)

本章目标包括：概述异常和异常处理，探索使用异常处理的优势，区分异常类型（`Error` (致命) vs. `Exception` (非致命)，以及受检异常 (checked) vs. 非受检异常 (unchecked)），在方法头中声明异常，在方法中抛出异常，编写 `try-catch` 块来处理异常，解释异常如何传播，从异常对象中获取信息，开发带有异常处理的应用程序，在 `try-catch` 块中使用 `finally` 子句，仅对意外错误使用异常，在 `catch` 块中重新抛出异常，创建链式异常，定义自定义异常类，发现文件/目录属性，删除和重命名文件/目录，并使用 `File` 类创建目录，使用 `PrintWriter` 类将数据写入文件，使用 `try-with-resources` 确保资源自动关闭，使用 `Scanner` 类从文件中读取数据，理解如何使用 `Scanner` 读取数据，开发一个在文件中替换文本的程序，从 Web 读取数据，开发一个 Web 爬虫。

### 1.3. 异常处理

* **传统方法缺点**：代码量大，正常逻辑与异常处理混杂，依赖返回值表示错误状态。
* **异常处理优势**：简洁地处理异常，分离正常逻辑和异常处理逻辑。

### 1.4. Exception-Handling Overview (异常处理概述)

程序运行时可能会出现运行时错误，导致程序异常终止。异常处理机制允许程序继续运行或优雅地终止。

### 1.5. Exception Advantages (异常优势)

异常处理的优势在于它允许方法向其调用者抛出异常。若无此能力，方法必须自行处理异常或终止程序。

### 1.6. Handling `InputMismatchException` (处理 InputMismatchException)

通过处理 `InputMismatchException`，程序将持续读取输入，直到输入正确为止。

### 1.7. Exception Types (异常类型)

Java 异常层次结构：

* `Object` -> `Throwable`
    * `Error` (系统错误，通常无法恢复)
    * `LinkageError`
    * `VirtualMachineError`等
    * `Exception` (程序错误或外部情况，可捕获和处理)
        * **受检异常 (Checked Exceptions)**：
            * `ClassNotFoundException`
            * `IOException`
        * **非受检异常 (Unchecked Exceptions)(`RuntimeException` 及其子类)**：
            * `ArithmeticException`
            * `NullPointerException`
            * `IndexOutOfBoundsException`
            * `IllegalArgumentException`

### 1.8. System Errors (系统错误)

系统错误由 JVM 抛出，并由 `Error` 类表示。`Error` 类描述内部系统错误。这些错误很少发生，如果发生，通常除了通知用户并尝试优雅地终止程序外，几乎无能为力。

### 1.9. Exceptions (异常)

`Exception` 描述由程序和外部情况引起的错误。这些错误可以被捕获和处理。

### 1.10. Runtime Exceptions (运行时异常)

`RuntimeException` 是由编程错误引起的，例如错误的类型转换、数组越界访问和数值错误。Java 不强制要求捕获非受检异常。

### 1.11. Checked Exceptions vs. Unchecked Exceptions (受检异常 vs. 非受检异常)

`RuntimeException`、`Error` 及其子类被称为**非受检异常**。所有其他异常被称为**受检异常**，这意味着编译器强制程序员检查和处理这些异常。

### 1.12. Unchecked Exceptions (非受检异常)

在大多数情况下，非受检异常反映了不可恢复的编程逻辑错误。例如，如果你在对象被赋值之前通过引用变量访问对象，就会抛出 `NullPointerException`；如果你访问数组中越界元素，就会抛出 `IndexOutOfBoundsException`。这些是应该在程序中纠正的逻辑错误。为了避免 `try-catch` 块的繁琐过度使用，Java 不强制编写代码来捕获非受检异常。

### 1.13. Declaring, Throwing, and Catching Exceptions (声明、抛出和捕获异常)

* **声明异常**：方法头使用 `throws` 关键字声明可能抛出的异常。
* **抛出异常**：程序检测到错误时，创建异常实例并用 `throw` 关键字抛出。
* **捕获异常**：使用 `try-catch` 块处理异常。

### 1.14. Declaring Exceptions (声明异常)

每个方法必须声明它可能抛出的受检异常类型。这被称为**声明异常**。

### 1.15. Throwing Exceptions (抛出异常)

当程序检测到错误时，程序可以创建适当的异常类型实例并抛出它。这被称为**抛出异常**。

### 1.16. Throwing Exceptions Example (抛出异常示例)

在 `setRadius` 方法中，如果 `newRadius` 小于 0，则抛出 `IllegalArgumentException`。

```java
public void setRadius (double newRadius) throws IllegalArgumentException {
 if (newRadius >= 0)
 radius = newRadius;
 else
 throw new IllegalArgumentException("Radius cannot be negative");
}
```

### 1.17. Catching Exceptions (捕获异常)

使用 `try-catch` 块捕获和处理异常。

```java
try {
 statements; // 可能抛出异常的语句
} catch (Exception1 exVar1) {
 handler for exception1;
} catch (Exception2 exVar2) {
 handler for exception2;
} // ...
catch (ExceptionN exVar3) {
 handler for exceptionN;
}
```

### 1.18. Catch or Declare Checked Exceptions (捕获或声明受检异常)

Java 强制你处理受检异常。如果方法声明了一个受检异常（即除了 `Error` 或 `RuntimeException` 之外的异常），你必须在 `try-catch` 块中调用它，或者在调用方法中声明抛出该异常。

### 1.19. Example: Declaring, Throwing, and Catching Exceptions (示例：声明、抛出和捕获异常)

本示例演示了如何通过修改 `Circle` 类中的 `setRadius` 方法来声明、抛出和捕获异常，当半径为负数时抛出异常。

### 1.20. Rethrowing Exceptions (重新抛出异常)

在 `catch` 块中处理部分异常后，可以选择重新抛出异常，以便更高级别的调用者也能处理。

```java
try {
 statements;
} catch(TheException ex) {
 perform operations before exits;
 throw ex; // 重新抛出异常
}
```

### 1.21. The `finally` Clause (finally 子句)

`finally` 块中的语句无论是否发生异常，都会被执行。它常用于清理资源，如关闭文件或网络连接。

```java
try {
 statements;
} catch (TheException ex) {
 handling ex;
} finally {
 finalStatements; // 总是执行的语句
}
```

### 1.22. Trace a Program Execution (追踪程序执行)

程序执行的追踪演示了 `try-catch-finally` 块的行为：

* **无异常**：`try` 块执行 -> `finally` 块执行 -> 后续语句执行。
* **异常被捕获**：`try` 块中发生异常 -> 匹配的 `catch` 块处理 -> `finally` 块执行 -> 后续语句执行。
* **异常被重新抛出**：`try` 块中发生异常 -> 匹配的 `catch` 块处理并重新抛出 -> `finally` 块执行 -> 异常传播给调用者。

### 1.23. Cautions When Using Exceptions (使用异常时的注意事项)

异常处理将错误处理代码与正常编程任务分离，使程序更易读和维护。然而，异常处理通常需要更多时间和资源，因为它需要实例化新的异常对象、回滚调用栈以及将错误传播给调用方法。

### 1.24. When to Throw Exceptions (何时抛出异常)

当一个方法中发生异常，如果你希望该异常由其调用者处理，则应该创建并抛出异常。如果该异常可以在发生的方法中处理，则无需抛出。

### 1.25. When to Use Exceptions (何时使用异常)

`try-catch` 块应用于处理**意外错误条件**。不应将其用于处理简单、预期的情况。例如，检查引用变量是否为 `null`，使用 `if (refVar != null)` 比 `try-catch (NullPointerException)` 更高效。

### 1.26. Defining Custom Exception Classes (定义自定义异常类)

* 尽可能使用 API 中已有的异常类。
* 当预定义的类不足以满足需求时，定义自定义异常类。
* 自定义异常类通过扩展 `Exception` 或 `Exception` 的子类来实现。

### 1.27. Custom Exception Class Example (自定义异常类示例)

在 `setRadius` 方法中，如果半径为负数，可以抛出自定义异常类（如 `InvalidRadiusException`）。

### 1.28. The File Class (File 类)

`File` 类旨在提供文件或路径的抽象，处理机器相关的复杂性，提供机器无关的方式。文件名是一个字符串。`File` 类是文件名称及其目录路径的包装类。

### 1.29. Obtaining file properties and manipulating file (获取文件属性和操作文件)

`java.io.File` 类提供了以下功能：

* **构造函数**：通过路径名、父目录和子文件路径创建 `File` 对象。
* **文件属性查询**：`exists()`、`canRead()`、`canWrite()`、`isDirectory()`、`isFile()`、`isAbsolute()`、`isHidden()`、`getAbsolutePath()`、`getCanonicalPath()`、`getName()`、`getPath()`、`getParent()`、`lastModified()`、`length()`。
* **文件操作**：`listFile()` (列出目录下的文件)、`delete()` (删除)、`renameTo()` (重命名)、`mkdir()` (创建目录)、`mkdirs()` (创建多级目录)。

### 1.30. Problem: Explore File Properties (问题：探索文件属性)

本节的目标是编写一个程序，演示如何以平台无关的方式创建文件，并使用 `File` 类的方法获取其属性。

### 1.31. Text I/O (文本I/O)

`File` 对象封装文件属性，但不包含读写数据方法。要执行 I/O 操作，需要使用相应的 Java I/O 类创建对象。本节介绍如何使用 `Scanner` 和 `PrintWriter` 类读写文本文件中的字符串和数值。

### 1.32. Writing Data Using PrintWriter (使用 PrintWriter 写入数据)

`java.io.PrintWriter` 类用于将数据写入文本文件。

* **构造函数**：根据文件名创建 `PrintWriter` 对象。
* **写入方法**：`print()` 和 `println()` 系列方法，可写入字符串、字符、各种基本数据类型和字符数组。
* `println` 会在写入后添加行分隔符（在 Windows 上是 `\r\n`，在 Unix 上是 `\n`）。
* 也支持 `printf` 格式化输出。

### 1.33. Try-with-resources (try-with-resources)

JDK 7 引入了 `try-with-resources` 语法，确保资源（如文件）在块结束时自动关闭，避免程序员忘记关闭文件。

```java
try (declare and create resources) {
 Use the resource to process the file;
}
```

### 1.34. Reading Data Using Scanner (使用 Scanner 读取数据)

`java.util.Scanner` 类用于从文件或字符串中读取数据。

* **构造函数**：可以从 `File` 或 `String` 创建 `Scanner` 对象。
* **关闭**：`close()` 关闭此扫描器。
* **读取方法**：`hasNext()` (检查是否有下一个 token)、`next()` (返回下一个 token，作为字符串)、`nextByte()`、`nextShort()`、`nextInt()`、`nextLong()`、`nextFloat()`、`nextDouble()`。
* `useDelimiter()`：设置扫描器的分隔模式。

### 1.35. Problem: Replacing Text (问题：替换文本)

本节的目标是编写一个名为 `ReplaceText` 的类，用新字符串替换文本文件中的字符串。文件名和字符串作为命令行参数传入。

### 1.36. Reading Data from the Web (从 Web 读取数据)

可以像从本地文件读取数据一样，从 Web 读取数据。
通过 `URL` 对象和 `openStream()` 方法获取输入流，然后用 `Scanner` 读取数据。

### 1.37. Case Study: Web Crawler (案例研究：网络爬虫)

本案例研究开发一个通过跟踪超链接来遍历 Web 的程序。为了避免重复访问 URL，程序维护两个 URL 列表：一个存储待遍历的 URL，另一个存储已遍历的 URL。

## 2. Chapter 13: Abstract Classes and Interfaces (抽象类和接口)

### 2.1. Motivations (动机)

在开发 GUI 程序时，需要编写代码来响应用户操作。这需要了解接口，接口用于定义类的共同行为。在深入接口之前，本章将介绍抽象类。

### 2.2. Objectives (目标)

本章目标包括：设计和使用抽象类，使用抽象 `Number` 类泛化数值包装类，使用 `Calendar` 和 `GregorianCalendar` 处理日历，使用接口指定对象共同行为，实现 `Comparable` 接口定义自然顺序，使用 `Cloneable` 接口进行对象克隆，探讨具体类、抽象类和接口异同，设计 `Rational` 类处理有理数，以及遵循类设计指南。

### 2.3. Abstract Classes and Abstract Methods (抽象类和抽象方法)

* **抽象类**：用 `abstract` 关键字修饰的类。抽象类不能被实例化，但可以作为其他类的父类。
* **抽象方法**：用 `abstract` 关键字修饰且没有方法体的方法。抽象方法只能在抽象类中声明。
* UML 图中，抽象类名和抽象方法用斜体表示。

### 2.4. Abstract method in abstract class (抽象类中的抽象方法)

抽象方法不能包含在非抽象类中。如果抽象超类的子类没有实现所有的抽象方法，那么子类也必须被定义为抽象类。换句话说，从抽象类继承的非抽象子类，必须实现所有抽象方法，即使这些方法在子类中没有被直接使用。

### 2.5. Object cannot be created from abstract class (抽象类不能创建对象)

抽象类不能使用 `new` 运算符实例化。然而，你可以定义其构造函数，这些构造函数会在其子类的构造函数中被调用。

### 2.6. Abstract class without abstract method (没有抽象方法的抽象类)

可以定义一个不包含任何抽象方法的抽象类。在这种情况下，你不能使用 `new` 运算符创建该类的实例。这个类被用作定义新子类的基类。

### 2.7. Superclass of abstract class may be concrete (抽象类的超类可以是具体类)

一个子类可以是抽象的，即使它的超类是具体的。例如，`Object` 类是具体的，但它的子类（如 `GeometricObject`）可以是抽象的。

### 2.8. Concrete method overridden to be abstract (具体方法被重写为抽象方法)

子类可以重写其超类中的具体方法并将其定义为抽象方法。这种情况比较罕见，但当超类中的方法实现对子类变得无效时会很有用。在这种情况下，子类必须被定义为抽象类。

### 2.9. Abstract class as type (抽象类作为类型)

你不能从抽象类创建实例，但抽象类可以作为数据类型使用。因此，创建元素类型为 `GeometricObject` 的数组是正确的：`GeometricObject[] geo = new GeometricObject[10];`

### 2.10. Case Study: the Abstract Number Class (案例研究：抽象 Number 类)

`java.lang.Number` 是一个抽象类，是所有数值包装类（`Integer`, `Double`, `Long`, `Float`, `Short`, `Byte`, `BigInteger`, `BigDecimal`）的超类。它提供了将数值转换为各种基本数据类型（如 `byteValue()`, `shortValue()`, `intValue()`, `longValue()`, `floatValue()`, `doubleValue()`）的抽象方法。

### 2.11. The Abstract Calendar Class and Its GregorianCalendar subclass (抽象 Calendar 类及其 GregorianCalendar 子类)

`java.util.Date` 表示毫秒精度的时间瞬间。`java.util.Calendar` 是一个抽象基类，用于提取日期和时间信息（年、月、日、小时、分钟、秒）。`Calendar` 的子类可以实现特定的日历系统，如公历 (`GregorianCalendar`)。`java.util.GregorianCalendar` 是 Java API 中支持的公历实现。

### 2.12. The `get` Method in Calendar Class (Calendar 类中的 get 方法)

`Calendar` 类中的 `get(int field)` 方法用于提取日期和时间信息。`field` 参数是常量，表示要获取的日期/时间字段，如 `YEAR`, `MONTH`, `DATE`, `HOUR`, `MINUTE`, `SECOND` 等。

### 2.13. Interfaces (接口)

* **什么是接口？**：一个类似于类的构造，只包含常量和抽象方法。
* **为什么接口有用？**：它类似于抽象类，但其目的是指定对象的共同行为。
* **如何定义接口？**：使用 `public interface InterfaceName { constant declarations; abstract method signatures; }`
* **如何使用接口？**：通过类实现接口。

### 2.14. What is an interface? Why is an interface useful? (什么是接口？为什么接口有用？)

接口是类似于类的构造，只包含常量和抽象方法。它类似于抽象类，但其目的是指定对象的共同行为，即使这些类之间没有直接的继承关系。

### 2.15. Define an Interface (定义接口)

使用 `public interface InterfaceName { ... }` 语法定义接口。接口可以包含常量声明和抽象方法签名。

```java
public interface Edible {
 /** Describe how to eat */
 public abstract String howToEat();
}
```

### 2.16. Interface is a Special Class (接口是一种特殊类)

接口被视为 Java 中的特殊类。每个接口都被编译成独立的字节码文件。与抽象类类似，你不能使用 `new` 运算符创建接口的实例。但在大多数情况下，你可以像使用抽象类一样使用接口，例如，作为变量的数据类型或类型转换的结果。

### 2.17. Omitting Modifiers in Interfaces (接口中省略修饰符)

接口中所有数据字段默认都是 `public final static`，所有方法默认都是 `public abstract`。因此，这些修饰符可以省略。接口中定义的常量可以通过 `InterfaceName.CONSTANT_NAME` 语法访问。

### 2.18. Example: The Comparable Interface (示例：Comparable 接口)

`java.lang.Comparable<E>` 接口定义了对象的自然排序。它包含一个抽象方法：`public int compareTo(E o);` `compareTo` 方法返回负整数、零或正整数，分别表示当前对象小于、等于或大于指定对象。

### 2.19. The `toString`, `equals`, and `hashCode` Methods (toString、equals 和 hashCode 方法)

每个包装类都重写了 `Object` 类中的 `toString`、`equals` 和 `hashCode` 方法。所有数值包装类和 `Character` 类都实现了 `Comparable` 接口，因此它们也实现了 `compareTo` 方法。

### 2.20. Integer and BigInteger Classes (Integer 和 BigInteger 类)

`Integer` 和 `BigInteger` 类都实现了 `Comparable` 接口，以便它们的对象可以进行比较。

### 2.21. The `Cloneable` Interfaces (Cloneable 接口)

* **标记接口 (Marker Interface)**：`Cloneable` 是一个空接口，不包含常量或方法。
* 它的作用是标记一个类拥有某种期望的属性，即其对象可以被克隆。
* 实现了 `Cloneable` 接口的类的对象可以使用 `Object` 类中定义的 `clone()` 方法进行克隆。

### 2.22. Shallow vs. Deep Copy (浅拷贝 vs. 深拷贝)

* **浅拷贝 (Shallow Copy)**：只复制对象本身和其基本类型字段的值。如果对象包含引用类型字段，则只复制引用，不复制引用的对象。这意味着新对象和原对象会共享引用的子对象。
* **深拷贝 (Deep Copy)**：不仅复制对象本身，还递归地复制其引用的所有子对象，确保新对象与原对象完全独立。

### 2.23. Interfaces vs. Abstract Classes (接口 vs. 抽象类)

| 特性 | 抽象类 (Abstract class) | 接口 (Interface) |
|---|---|---|
| **变量** | 无限制（可以有各种类型） | 必须是 `public static final` 常量 |
| **构造函数** | 被子类通过构造函数链调用。不能直接实例化。 | 没有构造函数。不能实例化。 |
| **方法** | 无限制（可以有具体方法、抽象方法） | 必须是 `public abstract` 实例方法 |

### 2.24. Interfaces vs. Abstract Classes, cont. (接口 vs. 抽象类，续)

所有类都共享一个根——`Object` 类，但接口没有单一的根。接口也可以定义一个类型。接口类型的变量可以引用实现该接口的类的任何实例。如果类扩展一个接口，这个接口扮演着超类的角色。可以将接口用作数据类型，并进行类型转换。

### 2.25. Caution: conflict interfaces (注意：冲突接口)

在极少数情况下，一个类可能实现两个存在冲突信息的接口（例如，两个具有不同值的同名常量，或两个签名相同但返回类型不同的方法）。这种类型的错误将被编译器检测到。

### 2.26. Whether to use an interface or a class? (何时使用接口或类？)

* **抽象类和接口**都可以用于建模共同特征。
* **强“is-a”关系（父子关系）**应使用**类**来建模（例如，员工是人）。
* **弱“is-a”关系（一种属性）**应使用**接口**来建模（例如，所有字符串都是可比较的，所以 `String` 类实现 `Comparable` 接口）。
* 接口还可用于**规避单继承限制**，实现多重继承的效果。

### 2.27. The Rational Class (有理数类)

`Rational` 类（有理数）的 UML 图和方法，它实现了 `java.lang.Comparable` 接口并扩展了 `java.lang.Number` 抽象类。提供了有理数的算术运算（加、减、乘、除）以及获取分子、分母和字符串表示的方法。

### 2.28. Designing a Class (设计类)

* **内聚性 (Coherence)**：一个类应描述一个单一实体，所有操作应逻辑地支持一个内聚目的。
* **分离职责 (Separating responsibilities)**：职责过多的实体可拆分为多个类。例如，`String`、`StringBuilder`、`StringBuffer` 分别处理不可变、可变（非同步）和可变（同步）字符串。
* **重用性**：设计类时应考虑其在不同组合、顺序和环境下的重用性。属性应可按任意顺序设置，方法应独立于其调用顺序。
* 提供**公共无参构造函数**，并尽可能重写 `Object` 类中的 `equals` 和 `toString` 方法。
* 遵循标准 Java 编程风格和命名约定。选择信息丰富的名称。
* 始终将数据声明放在构造函数之前，构造函数放在方法之前。
* 始终提供构造函数并初始化变量，以避免编程错误。

### 2.29. Using Visibility Modifiers (使用可见性修饰符)

* 类可以呈现两种契约：一种面向用户，一种面向扩展者。
* 数据字段应为 `private`，访问器方法（`get`）应为 `public`，供用户访问。
* 数据字段或方法若意在供扩展者使用，可设为 `protected`。
* 扩展者可以提高实例方法的可见性（如从 `protected` 到 `public`），或改变其实现，但不应以违反契约的方式改变。

### 2.30. Using the `static` Modifier (使用 static 修饰符)

由类的所有实例共享的属性应声明为 `static` 属性。
