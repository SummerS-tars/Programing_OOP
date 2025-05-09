# Java异常处理问题分析

您的代码出现了"Unhandled exception type NotTimeException"错误，这是因为`CheckTime()`方法抛出了一个检查型异常(checked exception)，但没有在方法签名中声明它。

## 问题分析

在Java中，所有检查型异常(继承自Exception但不是`RuntimeException`的子类)必须满足以下条件之一：

1. 在方法签名中使用`throws`关键字声明
2. 在方法内部用try-catch块捕获

## 解决方案

修改`CheckTime()`方法，在方法签名中声明它可能抛出`NotTimeException`：

```java
public void CheckTime() throws NotTimeException
{
    if(i < 10)
        throw new NotTimeException("not time yet", i);
}
```

这样，编译器就知道这个方法可能抛出`NotTimeException`，调用它的方法必须处理这个异常(正如您的`main`方法中已经做的那样)。

## 其他可能的解决方案

1. 将`NotTimeException`设计为非检查型异常(继承`RuntimeException`)
2. 在`CheckTime()`方法内部捕获异常(不推荐，因为您的`main`方法已经有适当的异常处理逻辑)

这个修改将使代码可以正确编译和运行，同时保持您原有的异常处理流程。
