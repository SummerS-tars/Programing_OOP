# 各种工具使用记录

- [1. .gitignore使用](#1-gitignore使用)
- [2. git库的基础指令](#2-git库的基础指令)
- [3. markdownlint使用](#3-markdownlint使用)

## 1. .gitignore使用  

.gitignore 文件用于告诉 Git 哪些文件或目录应该被忽略，不纳入版本控制。以下是一些常见的用法：  

1. 忽略特定文件：  

    ```gitignore
    secret.txt  
    ```

2. 忽略特定目录：  

    ```gitignore
    logs/
    ```

3. 忽略某种类型的文件：  

    ```gitignore
    *.log
    ```

4. 忽略某个目录下的所有文件，但不忽略该目录：  

    ```gitignore
    folder/*  
    !folder/
    ```

5. 忽略所有文件，但不忽略特定文件：  

    ```gitignore
    *
    !important.txt
    ```

6. 你可以使用以下命令将文件添加到 .gitignore 文件中：  
   1. **Git: 添加到 .gitignore** - 将文件添加到 .gitignore 文件中。  
   2. **Add gitignore** - 添加一个新的 .gitignore 文件。  

## 2. git库的基础指令

删除等操作和DOS操作还是挺像的  

## 3. markdownlint使用

有些时候markdownlint的规则实在是无法和其他插件调和  
我们可以通过创建`.json`文件进行个性化定制  

具体方法：

1. 创建文件：  
    命名：`.markdownlint.json`  
2. 编写`.json`文件  
    示例：(将MD007中对于无序列表的多级每层的缩进由默认的2改为4)  

    ```json
    {
        "MD007": {  
            "indent": 4
        }
    }
    ```
