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
Please help me to analyze the project lab6 based on the existing code

several prepared attention points:

what i need to do is the requirements in lab6.md
in the later development, I need to use JavaFX to right a gui for this project
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
3. [x] arbitrary barry position `#` implementation  
4. [x] refactor some parts about wrong info report  
    using exception to report some wrong cmds  
5. [x] implement the `bomb` cmd  
6. [ ] implement the `playback` cmd  
    1. [ ] read the file line by line  
    2. [ ] parse the cmd and execute it
7. [ ] introduce the observer pattern to project to refactor the structure better  
8. [ ] update the `PrintTools`  
    1. [ ] new input position prompt  
    2. [ ] error input some little refactor  
    3. [ ] new cmd prompt  
9. [ ] refactor the method to get specific game(it's not good enough now)  
10. [x] quit may be no need to be passed to game  

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

### Barrier Position Implementation

only available in `Gomoku` class  
use hash set to avoid duplicate position  

### Bomb Command Implementation

to make the GameMode able to receive the bomb cmd  
refactor the receiveOperation method  
by using a wrapper class `Operation`  

### Playback Command Implementation

we find a question that our pattern need to type a enter  
to start next input  
this may interfere the playback script  

we have several options maybe:  

1. modify the script  
2. change the input logic

maybe I want to try the first  

as the input from scripts still need to be parsed  
so the command execution is not in the GameMode classes of course  
maybe in the GameContainer?  
or we can create a new class to handle the playback  

overall processes:  

1. read the cmd  
    finished  
2. parse the cmd  
    finished  
3. try to find the script file  
    the imagination is to put them under the `playback/scripts` folder  
    Q1: how to specify this?  
4. execute the commands contained in the script file  
    Q2: how to read from the file?  
5. back to the normal game stream  

### Refactor

### Ask

Now I need to modify my lab project to support playback function

what I need to do first is to refactor the part about interact with user. The way it uses now is strictly bound with System.in stream.

so I want to do:

1. delete Scanner from PrintTools, it's not fit the functoins of the class
2. add command

### Observe Pattern

具体设计部分：  

架构已经调整过了，  
基础观察者模式使用的类、接口均放在`Observer`包下  

1. `Observer`  
2. `Subject`
    1. `BaseSubject`  

建立`View`层  

1. `ConsoleView`  
2. `GraphicView`待定  

目前考虑：  

1. `Subject`改造:  
    1. `GameLoop`  
2. `Observer`新增:  
    1. `ConsoleView`  
3. `GameContainer`与`GameList`彻底static化  

职责变更:  

1. `GameContainer`负责用来进行通知  
    1. 运行游戏相关状态迁移至`GameList`  
2. `Command`相关类  
    1. 不再负责视图相关的部分?  

设想流程：  

1. `Main`  
    1. 使用`GameList`以及`GameContainer`  
    2. 根据启动参数分配视图层实例  
    3. 根据启动参数分配不同的输入流
2. `GameContainer`  

Observer:  

`update`接口设计:  

第一种:  
用于需要非具体实例信息的  

1. `InputType`  
2. `CommandResult`  

第二种：  
用于获取具体需要具体实例信息的  

1. `InputType`  
2. `CommandResult`  
3. `GameMode`  

## 命令输入重构

playback实现

为了后面方便实现和GUI，这里考虑一下  
如何实现playback换输入流？  
根据CLI或者GUI模式获得不同的输入流  
同时游戏中输入playback命令后获取从文件的输入流  
