package top.thesumst.persistence;

import com.google.gson.*;
import top.thesumst.core.container.GameContainer;
import top.thesumst.core.container.GameList; // Import GameList
import top.thesumst.core.mode.GameMode;
import top.thesumst.type.Operation;
import top.thesumst.type.component.Player;
import top.thesumst.type.component.ChessBoard;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Stack;
import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import top.thesumst.type.OperationType;

/**
 * 游戏状态序列化器
 * 负责将整个游戏状态保存到文件和从文件恢复
 */
public class GameStateSerializer {
    
    private static final String SAVE_DIRECTORY = "saves/";
    private static final String SAVE_EXTENSION = ".gamesave";
    private static final Gson gson;
      static {
        // 配置Gson以处理复杂对象序列化
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(Point.class, new PointSerializer())
                .registerTypeAdapter(Stack.class, new StackSerializer())
                .registerTypeAdapter(Operation.class, new OperationSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // 添加LocalDateTime适配器
                .registerTypeAdapter(GameList.class, new GameListSerializer()) // Add GameListSerializer
                .registerTypeHierarchyAdapter(GameMode.class, new GameModeSerializer())
                .create();
    }
    
    /**
     * 保存游戏状态到文件
     * @param gameContainer 游戏容器
     * @param filename 保存文件名（不包含扩展名）
     * @throws IOException 保存失败时抛出
     */
    public static void saveGameState(GameContainer gameContainer, String filename) throws IOException {
        createSaveDirectory();
        
        GameSaveData saveData = new GameSaveData();
        saveData.gameList = GameContainer.getGameList(); // 假设添加此方法
        saveData.currentGameOrder = GameContainer.getCurrentGameOrder();
        saveData.saveTime = LocalDateTime.now();
        saveData.version = "1.0";
        
        String fullPath = SAVE_DIRECTORY + filename + SAVE_EXTENSION;
        try (FileWriter writer = new FileWriter(fullPath, java.nio.charset.StandardCharsets.UTF_8)) {
            gson.toJson(saveData, writer);
        }
    }
    
    /**
     * 从文件恢复游戏状态
     * @param filename 保存文件名（不包含扩展名）
     * @return 恢复的游戏保存数据
     * @throws IOException 读取失败时抛出
     */
    public static GameSaveData loadGameState(String filename) throws IOException {
        String fullPath = SAVE_DIRECTORY + filename + SAVE_EXTENSION;
        try (FileReader reader = new FileReader(fullPath, java.nio.charset.StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, GameSaveData.class);
        }
    }
    
    /**
     * 获取所有保存文件列表
     * @return 保存文件名列表（不包含扩展名）
     */
    public static String[] getSaveFileList() {
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            return new String[0];
        }
        
        File[] files = saveDir.listFiles((_, name) -> name.endsWith(SAVE_EXTENSION));
        if (files == null) {
            return new String[0];
        }
        
        String[] saveFiles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            saveFiles[i] = name.substring(0, name.length() - SAVE_EXTENSION.length());
        }
        return saveFiles;
    }
    
    /**
     * 自动生成保存文件名
     * @return 基于时间戳的文件名
     */
    public static String generateSaveFileName() {
        return "autosave_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
    
    private static void createSaveDirectory() {
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }
    
    /**
     * Point类的自定义序列化器
     */
    private static class PointSerializer implements JsonSerializer<Point>, JsonDeserializer<Point> {
        @Override
        public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("x", src.x);
            jsonObject.addProperty("y", src.y);
            return jsonObject;
        }
        
        @Override
        public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new Point(jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt());
        }
    }    /**
     * Stack类的自定义序列化器
     */
    private static class StackSerializer implements JsonSerializer<Stack<?>>, JsonDeserializer<Stack<?>> {
        @Override
        public JsonElement serialize(Stack<?> src, Type typeOfSrc, JsonSerializationContext context) {
            // 将栈转换为数组进行序列化
            JsonArray array = new JsonArray();
            for (Object obj : src) {
                if (obj instanceof Operation) {
                    // 对于操作类型，使用专门的序列化器
                    array.add(context.serialize(obj, Operation.class));
                } else {
                    // 其他类型正常序列化
                    array.add(context.serialize(obj));
                }
            }
            return array;
        }
        
        @Override
        public Stack<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            Stack<Object> stack = new Stack<>();
            JsonArray array = json.getAsJsonArray();
            
            // 确定Operation的参数化类型
            Type operationType = new com.google.gson.reflect.TypeToken<Operation<?>>() {}.getType();
            
            for (JsonElement element : array) {
                try {
                    // 尝试作为Operation反序列化，大多数情况下是这样
                    Operation<?> op = context.deserialize(element, operationType);
                    stack.push(op);
                } catch (JsonParseException e) {
                    // 如果失败，尝试作为普通对象反序列化
                    stack.push(context.deserialize(element, Object.class));
                }
            }
            
            return stack;
        }
    }    /**
     * Operation类的自定义序列化器
     */
    private static class OperationSerializer implements JsonSerializer<Operation<?>>, JsonDeserializer<Operation<?>> {
        @Override
        public JsonElement serialize(Operation<?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", src.getType().name());
            
            // 添加数据类型信息以便反序列化时确定正确的类型
            Object data = src.getData();
            if (data != null) {
                jsonObject.addProperty("dataType", data.getClass().getName());
                jsonObject.add("data", context.serialize(data));
            } else {
                jsonObject.addProperty("dataType", "null");
                jsonObject.add("data", JsonNull.INSTANCE);
            }
            
            return jsonObject;
        }
        
        @Override
        public Operation<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String typeName = jsonObject.get("type").getAsString();
            String dataTypeName = jsonObject.get("dataType").getAsString();
            JsonElement dataElement = jsonObject.get("data");
            
            // 根据操作类型和数据类型名恢复Operation对象
            OperationType operationType = OperationType.valueOf(typeName);
            
            try {
                if ("null".equals(dataTypeName) || dataElement.isJsonNull()) {
                    return new Operation<>(operationType, null);
                }
                
                // 根据数据类型名确定正确的类型
                Class<?> dataClass = determineDataClass(dataTypeName, operationType);
                Object data = context.deserialize(dataElement, dataClass);
                
                return new Operation<>(operationType, data);
            } catch (Exception e) {
                System.err.println("Warning: Failed to deserialize Operation: " + e.getMessage());
                e.printStackTrace();
                // 返回一个带有通用Object的Operation作为后备
                return new Operation<>(operationType, context.deserialize(dataElement, Object.class));
            }
        }
        
        /**
         * 根据数据类型名和操作类型确定正确的数据类
         */
        private Class<?> determineDataClass(String dataTypeName, OperationType operationType) throws ClassNotFoundException {
            // 尝试直接使用类名
            try {
                return Class.forName(dataTypeName);
            } catch (ClassNotFoundException e) {
                // 如果类名不正确，根据操作类型推断
                switch (operationType) {
                    case MOVE:
                        return Point.class; // 移动操作通常使用Point
                    case BOMB:
                        return Point.class; // 炸弹操作也通常使用Point
                    case PASS:
                        return Object.class; // PASS操作可能没有特定数据
                    default:
                        throw new ClassNotFoundException("Cannot determine data class for: " + dataTypeName);
                }
            }
        }
        
    }
    
    /**
     * GameMode类的自定义序列化器
     * 处理多态序列化问题
     */    
    private static class GameModeSerializer implements JsonSerializer<GameMode>, JsonDeserializer<GameMode> {
        @Override
        public JsonElement serialize(GameMode src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("className", src.getClass().getName());
            jsonObject.addProperty("gameOrder", src.gameOrder);
            jsonObject.addProperty("gameMode", src.gameMode);
            jsonObject.addProperty("size", src.size);
            jsonObject.add("board", context.serialize(src.getBoard()));
            jsonObject.add("player1", context.serialize(src.getPlayer1()));            
            jsonObject.add("player2", context.serialize(src.getPlayer2()));
            jsonObject.addProperty("isBlackTurn", src.isBlackTurn());
            jsonObject.addProperty("isOver", src.isOver());
            
            // 序列化操作栈
            // 使用已有的getter方法
            jsonObject.add("stepStack", context.serialize(src.getStepStack()));
            jsonObject.add("undoStack", context.serialize(src.getUndoStack()));
            
            // GomokuMode特有字段的序列化
            if (src.getClass().getName().equals("top.thesumst.core.mode.GomokuMode")) {
                try {
                    // 使用反射获取GomokuMode的炸弹数量字段
                    java.lang.reflect.Field blackBombNumField = src.getClass().getDeclaredField("blackBombNum");
                    java.lang.reflect.Field whiteBombNumField = src.getClass().getDeclaredField("whiteBombNum");
                    blackBombNumField.setAccessible(true);
                    whiteBombNumField.setAccessible(true);
                    
                    int blackBombNum = (int) blackBombNumField.get(src);
                    int whiteBombNum = (int) whiteBombNumField.get(src);
                    
                    jsonObject.addProperty("blackBombNum", blackBombNum);
                    jsonObject.addProperty("whiteBombNum", whiteBombNum);
                } catch (Exception e) {
                    System.err.println("Warning: Failed to serialize GomokuMode bomb numbers: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            return jsonObject;
        }
          @Override
        public GameMode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get("className").getAsString();
            
            try {
                // 获取基本信息
                int gameOrder = jsonObject.get("gameOrder").getAsInt();
                String gameMode = jsonObject.get("gameMode").getAsString();
                int size = jsonObject.get("size").getAsInt();
                boolean isBlackTurn = jsonObject.get("isBlackTurn").getAsBoolean();
                boolean isOver = jsonObject.get("isOver").getAsBoolean();
                
                // 反序列化Player对象
                Player player1 = context.deserialize(jsonObject.get("player1"), Player.class);
                Player player2 = context.deserialize(jsonObject.get("player2"), Player.class);
                
                // 根据类名创建对应的GameMode实例
                GameMode restoredGame = null;
                switch (className) {
                    case "top.thesumst.core.mode.PeaceMode":
                        restoredGame = new top.thesumst.core.mode.PeaceMode(
                            gameOrder, gameMode, size, 
                            player1.getName(), player2.getName(),
                            player1.getColor(), player2.getColor()
                        );
                        break;
                    case "top.thesumst.core.mode.ReversiMode":
                        restoredGame = new top.thesumst.core.mode.ReversiMode(
                            gameOrder, gameMode, size,
                            player1.getName(), player2.getName(),
                            player1.getColor(), player2.getColor()
                        );
                        break;
                    case "top.thesumst.core.mode.GomokuMode":
                        restoredGame = new top.thesumst.core.mode.GomokuMode(
                            gameOrder, gameMode, size,
                            player1.getName(), player2.getName(),
                            player1.getColor(), player2.getColor()
                        );
                        break;
                    default:
                        throw new JsonParseException("Unsupported GameMode class: " + className);
                }
                
                // 恢复棋盘状态
                if (jsonObject.has("board")) {
                    ChessBoard board = context.deserialize(jsonObject.get("board"), ChessBoard.class);
                    // 需要在GameMode中添加setBoard方法或使用反射
                    // 暂时使用反射设置board
                    try {
                        java.lang.reflect.Field boardField = GameMode.class.getDeclaredField("board");
                        boardField.setAccessible(true);
                        boardField.set(restoredGame, board);
                    } catch (Exception e) {
                        System.err.println("Warning: Failed to restore board state: " + e.getMessage());
                    }
                }                  
                
                // 恢复游戏状态
                try {
                    java.lang.reflect.Field isBlackTurnField = GameMode.class.getDeclaredField("isBlackTurn");
                    isBlackTurnField.setAccessible(true);
                    isBlackTurnField.set(restoredGame, isBlackTurn);
                    
                    java.lang.reflect.Field isOverField = GameMode.class.getDeclaredField("isOver");
                    isOverField.setAccessible(true);
                    isOverField.set(restoredGame, isOver);
                    
                    // GomokuMode特有字段的反序列化
                    if (className.equals("top.thesumst.core.mode.GomokuMode")) {
                        if (jsonObject.has("blackBombNum") && jsonObject.has("whiteBombNum")) {
                            int blackBombNum = jsonObject.get("blackBombNum").getAsInt();
                            int whiteBombNum = jsonObject.get("whiteBombNum").getAsInt();
                            
                            try {
                                java.lang.reflect.Field blackBombNumField = restoredGame.getClass().getDeclaredField("blackBombNum");
                                java.lang.reflect.Field whiteBombNumField = restoredGame.getClass().getDeclaredField("whiteBombNum");
                                blackBombNumField.setAccessible(true);
                                whiteBombNumField.setAccessible(true);
                                
                                blackBombNumField.set(restoredGame, blackBombNum);
                                whiteBombNumField.set(restoredGame, whiteBombNum);
                                
                                System.out.println("恢复GomokuMode炸弹数量 - 黑棋: " + blackBombNum + ", 白棋: " + whiteBombNum);
                            } catch (Exception e) {
                                System.err.println("Warning: Failed to restore GomokuMode bomb numbers: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Warning: GomokuMode loaded but bomb numbers not found in save file");
                        }
                    }
                    
                    // 恢复操作栈
                    if (jsonObject.has("stepStack")) {
                        // 使用类型令牌正确反序列化操作栈
                        Type operationStackType = new com.google.gson.reflect.TypeToken<Stack<Operation<?>>>() {}.getType();
                        Stack<Operation<?>> stepStack = context.deserialize(jsonObject.get("stepStack"), operationStackType);
                        
                        java.lang.reflect.Field stepStackField = GameMode.class.getDeclaredField("stepStack");
                        stepStackField.setAccessible(true);
                        stepStackField.set(restoredGame, stepStack);
                        System.out.println("恢复步骤栈，大小: " + stepStack.size());
                    }
                    
                    if (jsonObject.has("undoStack")) {
                        // 使用类型令牌正确反序列化撤销栈
                        Type operationStackType = new com.google.gson.reflect.TypeToken<Stack<Operation<?>>>() {}.getType();
                        Stack<Operation<?>> undoStack = context.deserialize(jsonObject.get("undoStack"), operationStackType);
                        
                        java.lang.reflect.Field undoStackField = GameMode.class.getDeclaredField("undoStack");
                        undoStackField.setAccessible(true);
                        undoStackField.set(restoredGame, undoStack);
                        System.out.println("恢复撤销栈，大小: " + undoStack.size());
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Failed to restore game state: " + e.getMessage());
                    e.printStackTrace();
                }
                  return restoredGame;
                
            } catch (Exception e) {
                throw new JsonParseException("Failed to deserialize GameMode: " + e.getMessage(), e);
            }
        }
    }
}
