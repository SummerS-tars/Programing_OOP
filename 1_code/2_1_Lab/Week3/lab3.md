# lab 3 : Multiple Boards Version

## Expected Changes

- receive part
    - 更好的支持输入（顺序可乱，大小写支持）
    - 支持换棋盘的输入
    考虑新增一个类用于记录输入状态，顺便把输入的功能也交给它
- board part

## Details Design

### new classes

1. ReceiveTool
    1. instance variables
        - `changeFlag` : boolean
        - `boardNum` : int
        - `goPoint` : Point
    2. methods
        - `receiveOperation` : 接受玩家操作(`boardNum`和`goPoint`)
        - `getBoardNum` : 返回`boardNum`
        - `getGoPoint` : 返回`goPoint`
        - `getChangeFlag` : 返回`changeFlag`
        - `setChangeFlag` : 设置`changeFlag`

### changed classes

1. ReversiGame
    1. instance variables
        - `boardNum` : int
        - `chessBoard` : ChessBoard[]
    2. add methods
        - `initGame` : 初始化棋盘
        - `receivePlayerInfo` : 接受玩家信息(`name`和`color`)
2. ChessBoard
    1. add instance variables
        - `gameStatus` : boolean
    2. change method
        - constructor : 将`Player`的`name`和`color`的参数初始化放到外部，通过构造器传入
        - `initBoard` : deprecated
        - `setGameStatus` : add `gameStatus` change
        - `getGameStatus` : add `gameStatus` return
3. Player
    1. change method
        - constructor : 接受`name`和`color`参数

## Bug Fix

**severe bugs**  

- [x] 输入最后一个棋盘序号时，意外退出程序  
    大致是因为越界访问，输入棋盘序号，为从1开始，而数组下标从0开始，所以需要减1
- [x] 没办法正确换边  
    checkTurn调用被注释掉了
- [x] 直接输入换行，会导致程序退出  
    没有处理输入为空的情况
- [x] 选颜色时的异常输入会导致崩溃  
    根据测试大概是只能处理数字选择错误，输入其他的就会崩溃  
    通过引入try-catch来解决，这个很好，以后学着多用用  
- [x] 棋盘游戏结束后`gameEndFlag`状态没有刷新为true  
    在`checkTurn`方法中更改，删除原来使用但现在无用的返回值，直接更改`gameEndFlag`的值  
- [x] 每个棋盘的玩家棋子信息可能统计反了（默认p1为黑子，p2为白子）  
    使用条件表达式来判断，传入对应棋子数量  

**little bugs**  

- [x] 输入玩家2名称时，没有实现刷新屏幕
- [x] 棋盘信息没有输出，最好为每个棋盘添加一个attribute，用于记录当前棋盘的序号
- [x] 棋盘序号输出时下标从0开始了
- [x] 两个玩家名字总是只和第一个一样
- [ ] zsh（至少这个是这样）游戏过程中部份清屏功能失效
