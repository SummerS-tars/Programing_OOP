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