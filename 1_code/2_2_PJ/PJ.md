# Project Development Document

## 主要开发功能记录

1. GUI  
    1. view
    2. controller
2. Persistent Save
    1. Save
    2. Load

## 开发计划

1. 基于Stage的instruction prompt开发
    1. [x] stage 1
    2. [x] stage 3
    3. [x] stage 2
    4. [x] stage 4
    5. [x] stage 5
    6. [x] stage 6
        1. [x] when we change the game, the symbols on the board are not presented only when we  do some operaion(may be problem caused by the GUIView's init() method)
        2. [x] the positions bombed in GomokuMode are not presented(it should be presented by some symbols, you can choose an emoji)
            javafx's bug, `@` not shown correctly in `setText`  
        3. [x] the info of the player is not shown(in reversi, the chess number; in gomoku, the rest bomb number)
        4. [x] we can put an chess after the corresponded player in the info col
        5. [x] quit only close the model part(in core), view and controller are not closed when we click the quit button.
    7. [x] stage 7
        1. [x] the save check not appear when I click quit button, instead it directly quit the game
        2. [x] the CLI save can't be correctly stored, the info belike

            ```txt
            程序即将退出...
            是否要保存当前游戏进度？
            1.  保存并退出
            2.  直接退出
            3.  取消退出
            请选择 (1-3): 1
            请输入存档名称: no
            输入无效，退出已取消。
            ```

        3. [x] cli quit will show the gui window to check whether to store save, and then it's own cli confirmation.

        4. [x] the save info of gameList is not right.

            ```json
            {
            "gameList": {},
            "currentGameOrder": 1,
            "saveTime": "2025-05-31T16:38:00.8278734",
            "version": "1.0"
            }
            ```

        5. [x] make clear how the GameList is constructed, now the save can be load, but the game will still create a new gameList. You should check how the gameList in the save be loaded into games
        6. [x] current game number isn't save or load correctly
        7. [x] the Stack of operation(in GameMode) is not saved in json, it's also important data.
        8. [x] CLI quit save check appears two times

            ```txt
            当前回合: test1 ○ : quit
            QuitCommand: 使用CLI模式处理退出
            是否要保存当前游戏进度？
            1. 保存并退出
            2.  直接退出
            3.  取消退出
            请选择 (1-3): 2

            程序即将退出...
            是否要保存当前游戏进度？
            1.  保存并退出
            2.  直接退出
            3.  取消退出
            请选择 (1-3): 2
            ```

        9. [x] the GameMode save lost some info like: Gomoku's bomb number is not saved. As a result, the number of the players' bomb number will refreshed every time after reloading the game from save.

    8. [x] stage 8
2. 微调  
3. 重构
    1. [x] GameContainer或许还是需要考虑移除
        1. [x] 将主要循环还是移入GameLoop  
        2. [x] GameContainer主要用于接收Command调控  
    2. [x] Command添加Invalid，更加统一逻辑  
        1. [x] 考虑修改Command的接口，统一从GameContainer中获取相关游戏  

4. 修复  
    1. [x] GameMode的Operation记录有问题  
