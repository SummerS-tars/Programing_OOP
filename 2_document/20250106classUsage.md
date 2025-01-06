# 笔记1 2025.1.6~7

- [1. Java基本文件格式与编译、执行方式](#1-java基本文件格式与编译执行方式)
  - [1.1. 基本文件格式](#11-基本文件格式)
  - [1.2. 编译与执行方式(目前仅局限于简单情况)](#12-编译与执行方式目前仅局限于简单情况)
- [2. Java中的class](#2-java中的class)
  - [2.1. 基础格式](#21-基础格式)
  - [2.2. 基础概念与规范](#22-基础概念与规范)

## 1. Java基本文件格式与编译、执行方式

### 1.1. 基本文件格式

1. 源文件:`.java`
2. 编译生成字节码文件:`.class`

### 1.2. 编译与执行方式(目前仅局限于简单情况)

以`A.java`文件为例(假设其中有A,B,C三个类)

1. 编译指令:```javac A.java```  
    经过编译会生成若干个`.class`字节码文件
2. 运行命令:```java A```or```java B```or```java C```  
    会将A or B or C放在JVM(java虚拟机)中运行 *(这时候要求三者中都要有`main`函数)*  
    注意java通过虚拟机实现同个编译文件可以跨平台运行,是通过虚拟机与操作系统直接沟通  

## 2. Java中的class

### 2.1. 基础格式

[getStart](../1_code/1_1_basicGrammar/getStarted.java)  

```java
class A
{

}
```

### 2.2. 基础概念与规范

[classTest](../1_code/1_1_basicGrammar/classTest.java)  

`public class`与`class`有别  
如下：  

1. 一个java源文件中可定义多个`class`  
2. 一个java源文件中`public class`非必须
3. 一个java源文件中只能有一个`public class`且必须与源文件名称一致  
4. 每个`class`中都可以定义`main`函数,并且只有存在`main`函数的时候,才可以被直接使用(指直接从命令台调用类)  
5. 使用`javac`命令编译,对应源文件中的每个类都会生成一个对应的字节码文件`.class`文件  
