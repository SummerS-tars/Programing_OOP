# 各种工具使用记录 2025.1.5

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
