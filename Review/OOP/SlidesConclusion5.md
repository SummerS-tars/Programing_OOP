# OOP

## 1. Chapter 17: Binary I/O (二进制 I/O)

### 1.1. Motivations (动机)

数据存储在文本文件中是人类可读的形式。数据存储在二进制文件中是二进制形式。你不能读取二进制文件。它们被设计为由程序读取。例如，Java 源代码存储在文本文件中，可以通过文本编辑器读取。而 Java 类存储在二进制文件中，由 JVM 读取。二进制文件的优势在于它们处理效率比文本文件更高。

### 1.2. Objectives (目标)

本章目标包括：了解 Java 中 I/O 处理方式，区分文本 I/O 和二进制 I/O，使用 `FileInputStream` 和 `FileOutputStream` 读写字节，使用 `DataInputStream` 和 `DataOutputStream` 读写基本类型值和字符串，使用 `ObjectOutputStream` 和 `ObjectInputStream` 存储和恢复对象（序列化），实现 `Serializable` 接口使对象可序列化，序列化数组，以及使用 `RandomAccessFile` 读写随机位置的文件。

### 1.3. How is I/O Handled in Java? (Java 中如何处理 I/O？)

`File` 对象封装文件或路径的属性，但不包含读写数据的方法。为了执行 I/O 操作，你需要使用适当的 Java I/O 类创建对象。这些对象包含读写文件数据的方法。本节介绍如何使用 `Scanner` 和 `PrintWriter` 类读写文本文件中的字符串和数值。

### 1.4. Text File vs. Binary File (文本文件 vs. 二进制文件)

数据存储在文本文件中是人类可读的形式。数据存储在二进制文件中是二进制形式。你不能读取二进制文件。二进制文件的优势在于它们处理效率比文本文件更高。
虽然在技术上不精确但正确地说，你可以想象文本文件由字符序列组成，二进制文件由位序列组成。例如，十进制整数 199 在文本文件中存储为三个字符 '1'、'9'、'9' 的序列，而在二进制文件中存储为字节类型值 C7，因为十进制 199 等于十六进制 C7。

### 1.5. Binary I/O (二进制 I/O)

文本 I/O 需要编码和解码。JVM 在写入字符时将 Unicode 转换为文件特定编码，在读取字符时将文件特定编码转换为 Unicode。二进制 I/O 不需要转换。当你向文件写入一个字节时，原始字节被复制到文件中。当你从文件读取一个字节时，返回精确的字节。

### 1.6. Binary I/O Classes (二进制 I/O 类)

Java I/O 流的层次结构：

* `Object` -> `Throwable`
    * `InputStream` (输入流基类)
        * `FileInputStream` (文件输入流)
        * `FilterInputStream` (过滤输入流)
            * `DataInputStream` (数据输入流)
            * `BufferedInputStream` (缓冲输入流)
        * `ObjectInputStream` (对象输入流)
    * `OutputStream` (输出流基类)
        * `FileOutputStream` (文件输出流)
        * `FilterOutputStream` (过滤输出流)
            * `DataOutputStream` (数据输出流)
            * `BufferedOutputStream` (缓冲输出流)
        * `ObjectOutputStream` (对象输出流)
        * `PrintStream` (打印流)

### 1.7. InputStream (输入流)

`java.io.InputStream` 是所有输入流的抽象基类。

* **`read()`**：读取下一个字节，返回 `int` 类型（0-255），如果到达文件末尾返回 -1。
* **`read(b: byte[])`**：读取字节到字节数组 `b`。
* **`read(b: byte[], off: int, len: int)`**：读取指定长度的字节到字节数组的偏移量处。
* 其他方法包括 `available()` (可用字节数)、`close()` (关闭流)、`skip()` (跳过字节)、`markSupported()` (是否支持标记)、`mark()` (标记当前位置)、`reset()` (重置到标记位置)。

### 1.8. OutputStream (输出流)

`java.io.OutputStream` 是所有输出流的抽象基类。

* **`write(int b)`**：将指定字节写入输出流。
* **`write(b: byte[])`**：将字节数组中的所有字节写入输出流。
* **`write(b: byte[], off: int, len: int)`**：将字节数组中指定范围的字节写入输出流。
* 其他方法包括 `close()` (关闭流)、`flush()` (刷新缓冲区)。

### 1.9. FileInputStream/FileOutputStream (文件输入/输出流)

* **`FileInputStream`** 和 **`FileOutputStream`** 将二进制输入/输出流与外部文件关联。
* 它们的方法都继承自其超类 `InputStream` 和 `OutputStream`。
* **`FileInputStream` 构造函数**：`public FileInputStream(String filename)` 或 `public FileInputStream(File file)`。
* **`FileNotFoundException`**：尝试创建不存在文件的 `FileInputStream` 时会发生此异常。
* **`FileOutputStream` 构造函数**：可以指定文件名/文件对象，以及是否以追加模式打开文件（`boolean append`）。
    * 如果文件不存在，则创建新文件。
    * 如果文件存在，默认情况下会覆盖其内容；若要追加内容，`append` 参数需设为 `true`。

### 1.10. FilterInputStream/FilterOutputStream (过滤输入/输出流)

* **过滤流**：用于过滤字节流。
* **目的**：将原始字节流包装起来，以提供更高级的读写功能（如读写整数、双精度浮点数或字符串）。
* **`FilterInputStream`** 和 **`FilterOutputStream`** 是过滤数据的基础类。
* `DataInputStream` 和 `DataOutputStream` 用于处理基本数值类型。

### 1.11. DataInputStream/DataOutputStream (数据输入/输出流)

* **`DataInputStream`**：从流中读取字节并将其转换为相应的基本类型值或字符串。
* **`DataOutputStream`**：将基本类型值或字符串转换为字节并输出到流中。

### 1.12. DataInputStream (数据输入流)

`DataInputStream` 扩展 `FilterInputStream` 并实现 `DataInput` 接口。
提供了读取各种基本数据类型（如 `boolean`, `byte`, `char`, `float`, `double`, `int`, `long`, `short`）和字符串 (`readLine()`, `readUTF()`) 的方法。

### 1.13. DataOutputStream (数据输出流)

`DataOutputStream` 扩展 `FilterOutputStream` 并实现 `DataOutput` 接口。
提供了写入各种基本数据类型和字符串 (`writeUTF()`) 的方法。

### 1.14. Characters and Strings in Binary I/O (二进制 I/O 中的字符和字符串)

* `writeChar(char c)` 写入 Unicode 字符（两字节）。
* `writeChars(String s)` 写入字符串中每个字符的 Unicode 形式。
* **UTF-8**：一种编码方案，可高效处理 ASCII 和 Unicode。
    * ASCII 值（<0x7F）编码为一字节。
    * Unicode 值（<0x7FF）编码为两字节。
    * 其他 Unicode 值编码为三字节。

### 1.15. Using DataInputStream/DataOutputStream (使用数据输入/输出流)

数据流作为现有输入/输出流的包装器，用于过滤数据。

* **构造函数**：`DataInputStream(InputStream instream)` 和 `DataOutputStream(OutputStream outstream)`。

### 1.16. Concept of pipe line (管道概念)

I/O 操作可以看作是数据通过一系列流进行处理的“管道”：
`DataInputStream` -> `FileInputStream` -> `External File` (读取)。
`DataOutputStream` -> `FileOutputStream` -> `External File` (写入)。

### 1.17. Order and Format (顺序和格式)

**注意**：必须以写入时的相同顺序和格式读取数据。
例如，如果使用 `writeUTF` 写入字符串，则必须使用 `readUTF` 读取。

### 1.18. Checking End of File (检查文件末尾)

**提示**：如果持续读取数据直到流末尾，可能会发生 `EOFException`。
可以使用 `input.available() == 0` 来检查是否到达文件末尾。

### 1.19. BufferedInputStream/BufferedOutputStream (缓冲输入/输出流)

`BufferedInputStream` 和 `BufferedOutputStream` 不包含新方法。它们的所有方法都继承自 `InputStream` 和 `OutputStream` 类。缓冲流通过使用缓冲区来加速 I/O 操作。

### 1.20. Constructing BufferedInputStream/BufferedOutputStream (构造缓冲输入/输出流)

* **`BufferedInputStream` 构造函数**：`public BufferedInputStream(InputStream in)` 或 `public BufferedInputStream(InputStream in, int bufferSize)`。
* **`BufferedOutputStream` 构造函数**：`public BufferedOutputStream(OutputStream out)` 或 `public BufferedOutputStream(OutputStream out, int bufferSize)`。

### 1.21. Case Studies: Copy File (案例研究：复制文件)

本案例研究开发了一个复制文件的程序。用户需要提供源文件和目标文件作为命令行参数。程序将复制源文件到目标文件，并显示文件中的字节数。如果源文件不存在，将提示用户文件未找到。如果目标文件已存在，将提示用户目标文件已存在。

### 1.22. Object I/O (对象 I/O)

`DataInputStream`/`DataOutputStream` 能够进行基本类型值和字符串的 I/O 操作。
`ObjectInputStream`/`ObjectOutputStream` 除了基本类型值和字符串，还能进行对象的 I/O 操作。

### 1.23. ObjectInputStream (对象输入流)

`ObjectInputStream` 扩展 `InputStream` 并实现 `ObjectInput` 和 `ObjectStreamConstants`。

* **`readObject()`**: 读取一个对象。

### 1.24. ObjectOutputStream (对象输出流)

`ObjectOutputStream` 扩展 `OutputStream` 并实现 `ObjectOutput` 和 `ObjectStreamConstants`。

* **`writeObject(o: Object)`**: 写入一个对象。

### 1.25. Using Object Streams (使用对象流)

你可以将 `ObjectInputStream`/`ObjectOutputStream` 包装在任何 `InputStream`/`OutputStream` 上。

* **`ObjectInputStream` 构造函数**：`public ObjectInputStream(InputStream in)`。
* **`ObjectOutputStream` 构造函数**：`public ObjectOutputStream(OutputStream out)`。

### 1.26. The Serializable Interface (Serializable 接口)

并非所有对象都能写入输出流。能写入对象流的对象被称为**可序列化**的。一个可序列化对象是 `java.io.Serializable` 接口的实例。因此，一个可序列化对象的类必须实现 `Serializable` 接口。
`Serializable` 接口是一个**标记接口**。它没有方法，所以你不需要在实现 `Serializable` 的类中添加额外的代码。
实现此接口使 Java 序列化机制能够自动化存储对象和数组的过程。

### 1.27. The `transient` Keyword (transient 关键字)

如果一个对象是 `Serializable` 的实例，但它包含不可序列化的实例数据字段，那么该对象可以被序列化吗？答案是不能。为了使对象可序列化，你可以使用 `transient` 关键字标记这些数据字段，告诉 JVM 在将对象写入对象流时忽略这些字段。

### 1.28. The `transient` Keyword, cont. (transient 关键字，续)

考虑以下类：

```java
public class Foo implements java.io.Serializable {
  private int v1;
  private static double v2;
  private transient A v3 = new A();
}
class A { } // A is not serializable
```

当 `Foo` 类的对象被序列化时，只有变量 `v1` 被序列化。变量 `v2` 没有被序列化，因为它是一个静态变量。变量 `v3` 没有被序列化，因为它被标记为 `transient`。如果 `v3` 没有被标记为 `transient`，就会发生 `java.io.NotSerializableException`。

### 1.29. Serializing Arrays (序列化数组)

如果数组的所有元素都可序列化，则该数组是可序列化的。因此，整个数组可以使用 `writeObject` 写入文件，然后使用 `readObject` 恢复。

### 1.30. Random Access Files (随机访问文件)

你目前使用的所有流都是只读或只写流。外部文件是顺序文件，不能在不创建新文件的情况下更新。通常需要修改文件或向文件插入新记录。Java 提供了 `RandomAccessFile` 类，允许在随机位置读写文件。

### 1.31. RandomAccessFile (随机访问文件)

`java.io.RandomAccessFile` 类提供了以下功能：

* **构造函数**：`RandomAccessFile(file: File, mode: String)` 或 `RandomAccessFile(name: String, mode: String)`。
* **`close()`**: 关闭流。
* **`getFilePointer()`**: 返回文件指针的当前偏移量。
* **`length()`**: 返回文件的长度。
* **`read()`**: 读取字节。
* **`read(b: byte[])`**: 读取字节到数组。
* **`read(b: byte[], off: int, len: int)`**: 读取指定长度的字节到数组。
* **`seek(pos: long)`**: 设置文件指针偏移量。
* **`setLength(newLength: long)`**: 设置文件长度。
* **`skipBytes(int n)`**: 跳过字节。
* **`write(b: byte[])`**: 写入字节数组。
* **`write(b: byte[], off: int, len: int)`**: 写入指定长度的字节数组。

### 1.32. File Pointer (文件指针)

随机访问文件由字节序列组成。有一个特殊的标记，称为**文件指针**，它定位在其中一个字节处。读或写操作发生在文件指针所在的位置。文件打开时，文件指针位于文件开头。当你读写数据时，文件指针会向前移动到下一个数据位置。例如，如果你使用 `readInt()` 读取一个 `int` 值，JVM 会从文件指针处读取四个字节，然后文件指针会比之前的位置向前移动四个字节。

### 1.33. RandomAccessFile Methods (随机访问文件方法)

`RandomAccessFile` 中的许多方法与 `DataInputStream` 和 `DataOutputStream` 中的方法相同。例如，`readInt()`, `readLong()`, `writeDouble()`, `readLine()`, `writeInt()`, 和 `writeLong()` 都可以用于数据输入流或数据输出流以及 `RandomAccessFile` 流。

### 1.34. RandomAccessFile Methods, cont. (随机访问文件方法，续)

* `void seek(long pos) throws IOException;` 设置从文件开头开始的偏移量，下一个读写操作将在此处进行。
* `long getFilePointer() throws IOException;` 返回文件指针的当前偏移量（以字节为单位），从文件开头开始，下一个读写操作将在此处进行。
* `long length() throws IOException` 返回文件的长度。
* `final void writeChar(int v) throws IOException` 将一个字符（两字节 Unicode）写入文件，高字节在前。
* `final void writeChars(String s) throws IOException` 将一个字符串作为字符序列写入文件。

### 1.35. RandomAccessFile Constructor (RandomAccessFile 构造函数)

* `RandomAccessFile raf = new RandomAccessFile("test.dat", "rw");` // 允许读写。
* `RandomAccessFile raf = new RandomAccessFile("test.dat", "r");` // 只读。

## 2. Chapter 19: Generics (泛型)

### 2.1. Objectives (目标)

本章目标包括：了解泛型的好处，使用泛型类和接口，声明泛型类和接口，理解泛型类型如何提高可靠性和可读性，声明和使用泛型方法和有界泛型类型，使用原始类型以实现向后兼容性，了解通配符类型以及它们为何必要，使用 JDK 1.5 泛型转换旧版代码，理解泛型类型信息被编译器擦除以及泛型类的所有实例共享相同的运行时类文件，了解由类型擦除引起的泛型类型限制，以及设计和实现泛型矩阵类。

### 2.2. Why Do You Get a Warning? (为什么会有警告？)

当你使用未指定泛型类型的集合（即使用原始类型）时，编译器会发出警告。例如：

```java
public class ShowUncheckedWarning {
  public static void main(String[] args) {
    java.util.ArrayList list = new java.util.ArrayList(); // 原始类型
    list.add("Java Programming");
  }
}
```

为了理解这条线上的编译警告，你需要学习 JDK 1.6 泛型。

### 2.3. Fix the Warning (修复警告)

通过指定泛型类型，可以消除编译警告。

```java
public class ShowUncheckedWarning {
  public static void main(String[] args) {
    java.util.ArrayList<String> list = new java.util.ArrayList<String>(); // 指定泛型类型
    list.add("Java Programming");
  }
}
```

现在没有编译警告了。

### 2.4. What is Generics? (什么是泛型？)

泛型是参数化类型的能力。通过此能力，你可以定义一个带有泛型类型（可以由编译器替换为具体类型）的类或方法。例如，你可以定义一个存储泛型类型元素的泛型栈类。从这个泛型类中，你可以创建一个存储字符串的栈对象和一个存储数字的栈对象。在这里，字符串和数字是替代泛型类型的具体类型。

### 2.5. Why Generics? (为什么使用泛型？)

泛型的主要好处是能够在编译时而非运行时检测到错误。泛型类或方法允许你指定类或方法可以使用的允许对象类型。如果你尝试将类或方法与不兼容的对象一起使用，就会发生编译错误。

### 2.6. Generic Type (泛型类型)

泛型接口示例：

```java
package java.lang;
public interface Comparable<T> { // 泛型接口
  public int compareTo (T o);
}
```

泛型实例化：在创建对象时指定具体类型。

### 2.7. Generic Instantiation (泛型实例化)

使用泛型类型可以提高可靠性并实现编译时错误检查。

* **Prior to JDK 1.5 (JDK 1.5 之前)**：
    `Comparable c = new Date();`
    `System.out.println(c.compareTo("red"));` // 可能导致运行时错误
* **JDK 1.5 (JDK 1.5 之后)**：
    `Comparable<Date> c = new Date();`
    `System.out.println(c.compareTo("red"));` // 编译错误 (因为 "red" 不是 Date 类型)

这提高了可靠性，并在编译时捕获错误。

### 2.8. Generic ArrayList in JDK 1.5 (JDK 1.5 中的泛型 ArrayList)

* **JDK 1.5 之前**：`java.util.ArrayList` 中的方法参数和返回值都是 `Object` 类型，需要手动类型转换。
* **JDK 1.5 之后**：`java.util.ArrayList<E>` 使用泛型，方法参数和返回值都是类型参数 `E`，无需手动类型转换。

### 2.9. No Casting Needed (无需类型转换)

使用泛型 `ArrayList` 时，无需进行显式类型转换，Java 会自动处理。

```java
ArrayList<Double> list = new ArrayList<>();
list.add(5.5); // 5.5 自动转换为 new Double(5.5)
list.add(3.0); // 3.0 自动转换为 new Double(3.0)
Double doubleObject = list.get(0); // 无需类型转换
double d = list.get(1); // 自动转换为 double
```

### 2.10. Declaring Generic Classes and Interfaces (声明泛型类和接口)

可以通过在类名或接口名后加上 `<E>` (或其他类型参数名) 来声明泛型类和接口。
例如：`GenericStack<E>`

* **数据字段**：`list: java.util.ArrayList<E>`
* **方法**：`GenericStack()` (构造函数)、`getSize(): int`、`peek(): E`、`pop(): E`、`push(o: E): void`、`isEmpty(): boolean`。

### 2.11. Generic Methods (泛型方法)

方法也可以声明为泛型，允许它们处理不同类型的参数。

```java
public static <E> void print(E[] list) { // 泛型方法
  for (int i = 0; i < list.length; i++)
    System.out.print(list[i] + " ");
  System.out.println();
}
```

对比非泛型方法，泛型方法提供了更好的类型安全和代码重用。

### 2.12. Bounded Generic Type (有界泛型类型)

泛型类型参数可以被限定在一个特定的类型或其子类型中。
语法：`<E extends GeometricObject>`
这意味着 `E` 必须是 `GeometricObject` 或其子类。

```java
public static <E extends GeometricObject> boolean // 有界泛型
  equalArea(E object1, E object2) {
    return object1.getArea() == object2.getArea();
  }
```

### 2.13. Raw Type and Backward Compatibility (原始类型和向后兼容性)

* **原始类型 (Raw Type)**：在泛型引入之前，不指定类型参数的泛型类型被称为原始类型。
* 例如：`ArrayList list = new ArrayList();`
* 这大致等同于 `ArrayList<Object> list = new ArrayList<Object>();`
* 原始类型是为了向后兼容 JDK 1.5 之前的代码而存在的。

### 2.14. Raw Type is Unsafe (原始类型不安全)

使用原始类型可能导致运行时错误，因为编译器无法在编译时进行严格的类型检查。

```java
public class Max {
  public static Comparable max(Comparable o1, Comparable o2) { ... }
}
Max.max("Welcome", 23); // 可能导致运行时错误，因为类型不兼容
```

运行时错误：`ClassCastException`，因为 `23` 无法与 `String` 进行比较。

### 2.15. Make it Safe (使其安全)

使用有界泛型类型可以确保类型安全，并在编译时捕获类型不兼容的错误。

```java
public class Max1 {
  public static <E extends Comparable<E>> E max(E o1, E o2) { ... } // 有界泛型
}
Max1.max("Welcome", 23); // 编译错误，因为 23 不是 Comparable<String>
```

### 2.16. Wildcards (通配符)

通配符（`?`）用于表示未知类型，增加泛型代码的灵活性。

* `?`：**无界通配符** (unbounded wildcard)，表示任何类型。
* `? extends T`：**有界通配符** (bounded wildcard)，表示 `T` 类型或其子类型。
* `? super T`：**下限通配符** (lower bound wildcard)，表示 `T` 类型或其超类型。

### 2.17. Generic Types and Wildcard Types (泛型类型和通配符类型)

这些图示展示了不同通配符类型之间的关系和层次结构。
`Object` 是所有类型的根。
`A<?>` 可以表示 `A<String>`、`A<Integer>` 等。
`A<? extends B>` 可以表示 `A<B>`、`A<B's subclass>`。
`A<? super B>` 可以表示 `A<B>`、`A<B's superclass>`。

### 2.18. Avoiding Unsafe Raw Types (避免不安全的原始类型)

为了类型安全，应始终使用泛型类型参数创建集合，而不是原始类型。

* 使用：`new ArrayList<ConcreteType>()`
* 而不是：`new ArrayList();`

### 2.19. Erasure and Restrictions on Generics (类型擦除和泛型限制)

泛型通过**类型擦除 (type erasure)** 实现。编译器使用泛型类型信息来编译代码，但之后会擦除它。因此，泛型信息在运行时不可用。这种方法使泛型代码能够与使用原始类型的旧版代码向后兼容。

### 2.20. Compile Time Checking (编译时检查)

编译器在编译时检查泛型是否正确使用，并将其转换为运行时使用的等效代码（使用原始类型）。

```java
// (a) 泛型代码
ArrayList<String> list = new ArrayList<>();
list.add("Oklahoma");
String state = list.get(0);

// (b) 编译后等效的原始类型代码
ArrayList list = new ArrayList();
list.add("Oklahoma");
String state = (String) (list.get(0));
```

编译器负责在编译时进行类型检查和类型转换的插入。

### 2.21. Important Facts (重要事实)

泛型类被其所有实例共享，无论其实际泛型类型如何。
例如，`GenericStack<String>` 和 `GenericStack<Integer>` 是两种类型，但 JVM 中只加载了一个 `GenericStack` 类。这是类型擦除的体现。

### 2.22. Restrictions on Generics (泛型限制)

1. **不能创建泛型类型的实例**：例如 `new E()` 不允许。
2. **不允许创建泛型数组**：例如 `new E[100]` 不允许。
3. **类的泛型类型参数不允许在静态上下文中使用**。
4. **异常类不能是泛型**。

### 2.23. Designing Generic Matrix Classes (设计泛型矩阵类)

目标：本示例提供一个泛型类，用于矩阵算术。该类实现了适用于所有类型矩阵的矩阵加法和乘法。

### 2.24. UML Diagram (UML 图)

`GenericMatrix<E extends Number>` 类的 UML 图，它是一个泛型类，用于矩阵操作。

* **数据字段**: `#add(element1: E, element2: E): E`、`#multiply(element1: E, element2: E): E`、`#zero(): E`。
* **方法**: `+addMatrix(...)`、`+multiplyMatrix(...)`、`+printResult(...)`。
* `IntegerMatrix` 和 `RationalMatrix` 是其子类。

### 2.25. Source Code (源代码)

目标：本示例提供两个程序，利用 `GenericMatrix` 类进行整数矩阵算术和有理数矩阵算术。

## 3. Chapter 20: Lists, Stacks, Queues, and Priority Queues (列表、栈、队列和优先队列)

### 3.1. Objectives (目标)

本章目标包括：探索 Java Collections Framework 层次结构中接口和类之间的关系，使用 `Collection` 接口中定义的常用方法操作集合，使用 `Iterator` 接口遍历集合中的元素以及使用 for-each 循环遍历集合，探讨如何以及何时使用 `ArrayList` 或 `LinkedList` 存储元素，使用 `Comparable` 接口和 `Comparator` 接口比较元素，使用 `Collections` 类中的静态实用方法进行排序、搜索、洗牌和查找集合中最大和最小元素，开发一个多重弹跳球应用程序使用 `ArrayList`，区分 `Vector` 和 `ArrayList` 以及使用 `Stack` 类创建栈，探索 `Collection`、`Queue`、`LinkedList` 和 `PriorityQueue` 之间的关系，并使用 `PriorityQueue` 类创建优先队列，以及使用栈编写程序来评估表达式。

### 3.2. What is Data Structure? (什么是数据结构？)

数据结构是按照某种方式组织的数据集合。该结构不仅存储数据，还支持访问和操作数据的功能。

### 3.3. Java Collection Framework hierarchy (Java 集合框架层次结构)

集合是存储一组对象的容器对象，通常称为元素。Java 集合框架支持三种类型的集合：列表 (lists)、集 (sets) 和映射 (maps)。

### 3.4. Java Collection Framework hierarchy, cont. (Java 集合框架层次结构，续)

`Set` 和 `List` 是 `Collection` 的子接口。
层次结构概览：

* `Collection` (接口)
    * `Set` (接口) -> `SortedSet` (接口) -> `NavigableSet` (接口) -> `TreeSet` (类)
    * `List` (接口) -> `AbstractList` (抽象类)
        * `ArrayList` (类)
        * `LinkedList` (类) -> `AbstractSequentialList` (抽象类) -> `Deque` (接口)
    * `Queue` (接口) -> `AbstractQueue` (抽象类)
        * `PriorityQueue` (类)
* 其他类：`AbstractCollection`、`AbstractSet`、`HashSet`、`LinkedHashSet`、`Vector`、`Stack`。

### 3.5. The Collection Interface (Collection 接口)

`Collection` 接口用于操作对象的集合。

* **`iterator()`**: 返回一个迭代器，用于遍历集合中的元素。
* **`forEach(action)`**: 对集合中的每个元素执行一个操作。
* **`add(e)`**: 向集合添加元素。
* **`addAll(c)`**: 将另一个集合的所有元素添加到当前集合。
* **`clear()`**: 删除所有元素。
* **`contains(o)`**: 检查是否包含特定元素。
* **`containsAll(c)`**: 检查是否包含另一个集合的所有元素。
* **`isEmpty()`**: 检查集合是否为空。
* **`remove(o)`**: 删除特定元素。
* **`removeAll(c)`**: 删除另一个集合的所有元素。
* **`retainAll(c)`**: 只保留与另一个集合共有的元素。
* **`size()`**: 返回元素数量。
* **`toArray()`**: 返回包含所有元素的数组。
* **`stream()` / `parallelStream()`**: 返回流（在 Chapter 23 中介绍）。

### 3.6. The List Interface (List 接口)

`List` 存储元素按顺序排列，并允许用户指定元素存储位置。用户可以通过索引访问元素。

### 3.7. The List Interface, cont. (List 接口，续)

`List` 接口扩展了 `Collection` 接口，并增加了基于索引的操作。

* **`add(index, element)`**: 在指定索引处添加元素。
* **`addAll(index, c)`**: 在指定索引处添加另一个集合的所有元素。
* **`get(index)`**: 返回指定索引处的元素。
* **`indexOf(o)`**: 返回第一个匹配元素的索引。
* **`lastIndexOf(o)`**: 返回最后一个匹配元素的索引。
* **`listIterator()`**: 返回列表迭代器。
* **`listIterator(startIndex)`**: 返回从指定索引开始的列表迭代器。
* **`remove(index)`**: 删除指定索引处的元素。
* **`set(index, element)`**: 设置指定索引处的元素。
* **`subList(fromIndex, toIndex)`**: 返回子列表。

### 3.8. The List Iterator (列表迭代器)

`java.util.ListIterator<E>` 扩展了 `Iterator<E>`，提供了双向遍历列表和修改列表的功能。

* **`add(element)`**: 向列表添加元素。
* **`hasPrevious()`**: 检查是否有上一个元素。
* **`nextIndex()`**: 返回下一个元素的索引。
* **`previous()`**: 返回上一个元素。
* **`previousIndex()`**: 返回上一个元素的索引。
* **`set(element)`**: 替换上次 `next()` 或 `previous()` 返回的元素。

### 3.9. ArrayList and LinkedList (ArrayList 和 LinkedList)

`ArrayList` 和 `LinkedList` 是 `List` 接口的具体实现。

* **`ArrayList`**：
    * 基于数组实现，支持通过索引进行随机访问（`get` 和 `set`）效率高。
    * 插入或删除非末尾元素效率低。
* **`LinkedList`**：
    * 基于链表实现，支持在任意位置插入或删除元素效率高。
    * 随机访问（`get` 和 `set`）效率低。
* 列表可以动态增长或收缩。数组一旦创建大小就固定。

### 3.10. The Comparator Interface (Comparator 接口)

有时需要比较不同类型的元素，或者元素不是 `Comparable` 类型。
`java.util.Comparator` 接口定义了比较两个对象的方法。

* **`compare(Object element1, Object element2)`**: 返回负数、零或正数，表示 `element1` 小于、等于或大于 `element2`。

### 3.11. Other Comparator Examples (其他比较器示例)

可以创建自定义 `Comparator` 来定义不同的排序规则，例如按字符串长度排序或忽略大小写排序。

### 3.12. The Collections Class (Collections 类)

`Collections` 类包含各种静态方法，用于操作集合和映射、创建同步集合以及创建只读集合。

### 3.13. The Collections Class UML Diagram (Collections 类 UML 图)

`java.util.Collections` 类提供了大量用于操作或返回集合的静态方法，包括：

* **排序**：`sort(list)`、`sort(list, comparator)`。
* **搜索**：`binarySearch(list, key)`、`binarySearch(list, key, comparator)`。
* **反转**：`reverse(list)`。
* **洗牌**：`shuffle(list)`、`shuffle(list, random)`。
* **复制**：`copy(dest, src)`。
* **频率/最大/最小**：`frequency(c, o)`、`max(c)`、`min(c)`。
* **不相交**：`disjoint(c1, c2)`。

### 3.14. Case Study: Multiple Bouncing Balls (案例研究：多重弹跳球)

这个案例研究开发了一个多重弹跳球的应用程序，可能使用了 `ArrayList` 来管理多个球对象。

### 3.15. The Vector and Stack Classes (Vector 和 Stack 类)

Java 集合框架是在 Java 2 中引入的。在此之前，`Vector` 和 `Stack` 等数据结构已受支持。这些类经过重新设计以适应 Java 集合框架，但其旧式方法仍保留以保持兼容性。

### 3.16. The Vector Class (Vector 类)

在 Java 2 中，`Vector` 与 `ArrayList` 类似，但 `Vector` 包含用于访问和修改向量的**同步方法**。目前引入的新集合数据结构都不是同步的。如果需要同步，可以使用集合类的同步版本。

### 3.17. The Vector Class, cont. (Vector 类，续)

`java.util.Vector<E>` 类提供了：

* **构造函数**：创建空向量、从集合创建、或指定初始容量和容量增量。
* **添加元素**：`addElement(o)`。
* **容量/大小**：`capacity()`、`size()`、`trimToSize()`、`setSize()`。
* **访问元素**：`elementAt(index)`、`firstElement()`、`lastElement()`。
* **枚举**：`elements()`。
* **插入/删除**：`insertElementAt()`、`removeAllElements()`、`removeElement()`、`removeElementAt()`、`setElementAt()`。

### 3.18. The Stack Class (Stack 类)

`Stack` 类表示一种**后进先出（LIFO）**的堆栈对象。元素只能从堆栈顶部访问。你可以从堆栈顶部检索、插入或删除元素。

* **`Stack()`**: 创建一个空栈。
* **`empty()`**: 检查栈是否为空。
* **`peek()`**: 返回栈顶元素但不删除。
* **`pop()`**: 返回并删除栈顶元素。
* **`push(o)`**: 将元素添加到栈顶。
* **`search(o)`**: 返回指定元素在栈中的位置。

### 3.19. Queues and Priority Queues (队列和优先队列)

**队列 (Queue)** 是一种**先进先出（FIFO）**的数据结构。元素在队列末尾追加，从队列开头删除。
在**优先队列 (Priority Queue)** 中，元素被分配了优先级。访问元素时，优先级最高的元素首先被删除。

### 3.20. The Queue Interface (Queue 接口)

`java.util.Queue<E>` 接口扩展了 `Collection<E>`，并定义了队列操作：

* **`offer(element)`**: 插入元素到队列。
* **`poll()`**: 检索并删除队列头，如果队列为空则返回 `null`。
* **`remove()`**: 检索并删除队列头，如果队列为空则抛出异常。
* **`peek()`**: 检索队列头但不删除，如果队列为空则返回 `null`。
* **`element()`**: 检索队列头但不删除，如果队列为空则抛出异常。

### 3.21. Using LinkedList for Queue (使用 LinkedList 作为队列)

`java.util.LinkedList<E>` 类实现了 `List<E>`、`Queue<E>` 和 `Deque<E>` 接口，因此可以作为队列使用。

### 3.22. The PriorityQueue Class (PriorityQueue 类)

`java.util.PriorityQueue<E>` 类实现了 `Queue<E>` 接口，提供优先队列功能。

* **构造函数**：
    * `PriorityQueue()`: 创建默认容量为 11 的优先队列。
    * `PriorityQueue(initialCapacity)`: 创建指定初始容量的优先队列。
    * `PriorityQueue(c: Collection)`: 从集合创建优先队列。
    * `PriorityQueue(initialCapacity, comparator)`: 创建指定初始容量和比较器的优先队列。

### 3.23. Case Study: Evaluating Expressions (案例研究：表达式求值)

栈可用于评估表达式。

### 3.24. Algorithm (算法)

**阶段 1：扫描表达式**
程序从左到右扫描表达式以提取操作数、运算符和括号。
1.1. 如果提取的项目是操作数，将其推入 `operandStack`。
1.2. 如果提取的项目是 `+` 或 `-` 运算符，处理 `operatorStack` 顶部所有操作符，然后将提取的操作符推入 `operatorStack`。
1.3. 如果提取的项目是 `*` 或 `/` 运算符，处理 `operatorStack` 顶部所有 `*` 或 `/` 操作符，然后将提取的操作符推入 `operatorStack`。
1.4. 如果提取的项目是 `(` 符号，将其推入 `operatorStack`。
1.5. 如果提取的项目是 `)` 符号，重复处理 `operatorStack` 顶部的操作符，直到遇到 `(` 符号。

**阶段 2：清空栈**
重复处理 `operatorStack` 顶部的操作符，直到 `operatorStack` 为空。

### 3.25. Example (示例)

表格详细展示了表达式求值算法的每一步，以及 `operandStack` 和 `operatorStack` 的状态变化。
