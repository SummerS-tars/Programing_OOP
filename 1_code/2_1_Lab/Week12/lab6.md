# OOP Lab 6

## New Features

1. 更大的五子棋棋盘（具体来说为15x15）  
    1. 行号采用1-F（十六进制）  
    2. 列号采用A-O（十六进制）  
2. 五子棋棋盘增设Barrier（符号`#`）  
    1. 障碍物所在位置无法落子
    2. 随机四个位置放置障碍物  
3. 五子棋模式增加道具Bomb（符号`@`）  
    1. 炸弹可用于移除对方任意一颗棋子，并切该位置变为弹坑（符号`@`），无法落子  
    2. 使用时机：己方回合代替落子  
    3. 初始炸弹数：白方3，黑方2  
    4. 使用方法：输入`@行列`（e.g. `@1A`）使用炸弹  
    5. 剩余炸弹数量要求显示在玩家状态列表中  
4. 演示模式
    1. 系统自动操作游戏以展示游戏玩法  
    2. 具体来说为使用`playback.cmd`中的内容进行演示  

## Advise from AI

prompt:  

```txt
Please help me to analyize the project lab6 based on the existing code

several prepared attention points:

what i need to do is the requirements in lab6.md
in the later devlopment, I need to use JavaFX to right a gui for this project
Attention several important question:

to realize my requirements, is the structure of the project right now good enough? where should be changed or improved?
introduce some basic information about JavaFX and to introduce it to my project, what should I do in brief?
```

## Implementation Thoughts

1. [x] test the size interface of the board  
2. [x] refactor the part about move attempt input  
    1. [x] del the feat to accept arbitrary sequence of input position  
    2. [x] add the feat to accept the input of `bomb` cmd  
    3. [x] add the `playback {filename.cmd}` cmd  
3. [ ] arbitrary barry position `#` implementation  
4. [x] refactor some parts about wrong info report  
    using exception to report some wrong cmds  
5. [ ] implement the `playback` cmd  
    1. [ ] read the file line by line  
    2. [ ] parse the cmd and execute it
6. [ ] introduce the observer pattern to project to refactor the structure better  
7. [ ] update the `PrintTools`  
    1. [ ] new input position prompt  
    2. [ ] error input some little refactor  
    3. [ ] new cmd prompt  
8. [ ] refactor the method to get specific game(it's not good now)  
9. [x] quit may be no need to be passed to game  

## Specification Implementation

### Size Interface for Gomoku Change

修改`GameList`中的新建模式  

### Input Parser Refactor and Update

显然，在新的需求之下，之前关于位置输入的解析功能已然不适用  

仔细分析一下新的需求：  

1. 要求行号固定在前，列号固定在后  
2. 行号使用十六进制，从1开始；列号使用字母表示，从A开始  
3. 新的需求：
    1. 炸弹模式 要在正常落子位置输入前加上`@`  
    2. 演示模式 支持`playback`命令  

再仔细考虑此需求  

那么我们棋盘必然拥有最大值15  
再大行号就需要做其他考虑了  
这时候若再使用16进制实在是难以理解，不如使用10进制  

出于对于良好品味的坚持  
依然考虑保留支持大小写混用  

那么我们需要重构的部分大概有几个  

1. 关于位置输入的正则表达式的调整  
2. 关于位置输入解析器的逻辑调整  
3. 关于位置输入的错误提示的调整  

需要新增的部分:  

1. 添加炸弹功能  
2. 添加演示功能  

#### 具体实现考虑

将输入位置的正则表达式考虑直接保存为字符串  
因为后面炸弹功能也要用  
好吧其实没什么必要  

回放功能也是要用的  
之前看漏了，除了命令本身，还需要一个参数，用于表示回放的文件名  
（这里还需要考虑一个问题就是路径问题，大概率是要放在某个项目文件夹中的，但是如何进行指定呢？不过不是这里输入解析功能实现需要担忧的）  

如何考虑输入呢？似乎是要在同一行输入，那么还是考虑使用正则表达式来匹配吧  

#### 一些问题及想法

可能想到一个问题：指令比较相近正确指令了，要不要更具体的提示？
但是考虑后关于此问题，  
我的所有可能的输入都有相关提示，那么假如输入错误了  
导致了未知命令的发生  
那么不是我应该具体告知的部分  
所以这里不进行具体的提示部分的实现  

### Bomb Command

1. add new info to `Player` class to store the bomb number?  
    not proper, only available in `Gomoku` class  
    so add to `GomokuMode` class  
    and add method to control it  
2. add legality test method for bomb cmd  
3. add command for use bomb cmd  
4. maybe need to refactor some part of the `move` method in `GomokuMode` class?

### Exception Handling Refactor

refactor the `GameMode` to make it support exception handling  

#### exception kinds

1. `IllegalMoveException`  
2. `IllegalCommandException`  

#### handling

all handled in the command classes  
report can carry some info  
which will be extracted to the result of command  
