# Java基础类库入门

- [1. import语句](#1-import语句)
- [2. System类](#2-system类)
    - [2.1. System.out类](#21-systemout类)
    - [2.2. System.in类](#22-systemin类)
- [3. java](#3-java)
    - [3.1. java.util包](#31-javautil包)
    - [3.2. java.io包](#32-javaio包)

## 1. import语句

**两种导入方式:**  

1. 明确导入(Specify Import):  

    ```java
    import java.util.Scanner ;
    ```

2. 通配符导入(Wildcard Import):  

    ```java
    import java.util.* ;
    ```

    通配符导入会导入`java.util`包中的所有类

**注意:**  
只有当程序中需要用到某个类时导入信息才会被编译器使用  
否则此类信息在编译时或运行时不会被读入  
两种生命方式在性能上没有什么区别  

**另外注意:**
java中一些重要的类库默认导入
例如`java.lang`，其包含类`System`、`String`、`Math`等

## 2. System类

Java使用`System.out`表示标准输出设备(默认为显示器)  
以`System.in`表示标准输入设备(默认为键盘)  

### 2.1. System.out类

1. `print`方法  
    输出后不换行  
2. `println`方法  
    `print line`,输出后换行  
3. `printf`方法  
    与C/C++中的printf类似

### 2.2. System.in类

一般结合`Scanner`类使用
表示从键盘输入数据  
用例见`Scanner`类[3.1 java.util](#31-javautil包)

## 3. java

### 3.1. java.util包

`java.util`包中包含了Java类库中的一些工具类  

- `Scanner`:  

    ```java
    java.util.Scanner sc = new java.util.Scanner(System.in) ;
    ```

    其中`System.in`表示标准输入设备(默认为键盘)  
    还可以换成例如`File`等其他输入  

    ```java
    java.util.Scanner sc = new java.util.Scanner(new java.io.File("test.txt")) ;
    ```

    - `next`方法  
        读取下一个字符串
    - `nextInt`方法  
        读取下一个整数  
    - `nextDouble`方法  
        读取下一个浮点数  
    - `nextLine`方法  
        读取下一行  
    - `close`方法  
        关闭输入流  

### 3.2. java.io包

`java.io`包中包含了Java类库中的一些输入输出类  

`File`:  

```java
java.io.File file = new java.io.File("test.txt") ;
```
