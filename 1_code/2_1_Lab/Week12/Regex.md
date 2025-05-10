# Regex

正则表达式(Regular Expression，简称regex)是一种用于描述字符串匹配模式的强大工具，常用于文本搜索、验证和替换操作。

## 正则表达式的基本概念

正则表达式是由普通字符和特殊字符（元字符）组成的字符序列，定义了一种搜索模式。这种模式可以用于文本搜索、提取和替换操作。

## 常用元字符和语法

- `.` - 匹配任意单个字符（除换行符外）
- `*` - 匹配前一个字符零次或多次
- `+` - 匹配前一个字符一次或多次
- `?` - 匹配前一个字符零次或一次
- `^` - 匹配字符串开头
- `$` - 匹配字符串结尾
- `[]` - 字符类，匹配括号内的任意一个字符
- `[^]` - 否定字符类，匹配不在括号内的任意字符
- `\d` - 匹配数字
- `\w` - 匹配字母、数字和下划线
- `\s` - 匹配空白字符
- `()` - 捕获组，可以提取匹配的内容
- `{n}` - 精确匹配n次
- `{n,}` - 至少匹配n次
- `{n,m}` - 匹配n到m次

## 支持正则表达式的语言

大多数现代编程语言都支持正则表达式，包括：

- Java
- JavaScript
- Python
- PHP
- C#/.NET
- Perl
- Ruby
- Go
- Swift
- Kotlin

## Java中的正则表达式

Java通过`java.util.regex`包提供正则表达式支持，主要包含两个核心类：

### 1. Pattern类

`Pattern`类表示编译后的正则表达式模式：

```java
// 编译正则表达式
Pattern pattern = Pattern.compile("正则表达式");

// 使用标志（如忽略大小写）
Pattern pattern = Pattern.compile("正则表达式", Pattern.CASE_INSENSITIVE);
```

### 2. Matcher类

`Matcher`类用于执行匹配操作：

```java
// 创建Matcher对象
Matcher matcher = pattern.matcher("要匹配的文本");

// 检查是否匹配
boolean found = matcher.matches();  // 整个字符串匹配
boolean found = matcher.find();     // 查找下一个匹配

// 提取匹配内容
if (matcher.find()) {
    String match = matcher.group();  // 整个匹配
    String group1 = matcher.group(1); // 第一个捕获组
}
```

### 3. 常见用法示例

#### 验证字符串格式

```java
boolean isValid = Pattern.matches("\\d{4}-\\d{2}-\\d{2}", "2025-05-11"); // 日期格式
```

#### 查找所有匹配项

```java
Pattern pattern = Pattern.compile("\\w+");
Matcher matcher = pattern.matcher("Hello Java Regex");
while (matcher.find()) {
    System.out.println(matcher.group());
}
```

#### 替换文本

```java
String result = "Hello World".replaceAll("World", "Java");
```

#### 分割字符串

```java
String[] parts = "a,b,c".split(",");
```

### 4. 实际案例解析

您代码中的例子很好地展示了正则表达式的应用：

```java
// 匹配棋盘位置，如A1或1A格式
private static final Pattern CHESS_POSITION = Pattern.compile(
    "([A-Za-z])([1-9]\\d{0,1})|([1-9]\\d{0,1})([A-Za-z])"
);
```

这个正则表达式解析：

- `([A-Za-z])` - 捕获一个字母
- `([1-9]\\d{0,1})` - 捕获1-99的数字
- `|` - 或者
- `([1-9]\\d{0,1})([A-Za-z])` - 捕获数字在前，字母在后的格式

`parsePosition`方法通过`matcher.group()`获取捕获组内容，体现了正则表达式在实际业务中的应用价值。

正则表达式虽然强大，但复杂表达式可能影响性能，建议在处理大量数据时谨慎使用，并考虑预编译模式以提高效率。
