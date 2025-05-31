package top.thesumst.persistence;

import com.google.gson.*;
import top.thesumst.core.container.GameList;
import top.thesumst.core.mode.GameMode;
import top.thesumst.type.ChessStatement;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class GameListSerializer implements JsonSerializer<GameList>, JsonDeserializer<GameList> {

    @Override
    public JsonElement serialize(GameList src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gameNumber", GameList.getGameNumber());
        jsonObject.addProperty("player1Name", GameList.getPlayer1Name());
        jsonObject.addProperty("player2Name", GameList.getPlayer2Name());
        jsonObject.add("player1Color", context.serialize(GameList.getPlayer1Color(), ChessStatement.class));
        jsonObject.add("player2Color", context.serialize(GameList.getPlayer2Color(), ChessStatement.class));
        
        // 确保games不为null
        List<GameMode> games = GameList.getGames();
        if (games == null) {
            games = new ArrayList<>();
        }
        jsonObject.add("games", context.serialize(games, List.class)); // Serialize the list of games
        
        return jsonObject;
    }    @Override
    public GameList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        GameList.startDeserialization(); // Signal start of deserialization

        JsonObject jsonObject = json.getAsJsonObject();

        try {
            // 设置静态字段
            GameList.setGameNumber(jsonObject.get("gameNumber").getAsInt());
            GameList.setPlayer1Name(jsonObject.get("player1Name").getAsString());
            GameList.setPlayer2Name(jsonObject.get("player2Name").getAsString());
            GameList.setPlayer1Color(context.deserialize(jsonObject.get("player1Color"), ChessStatement.class));
            GameList.setPlayer2Color(context.deserialize(jsonObject.get("player2Color"), ChessStatement.class));
            
            // Deserialize the list of games
            // Ensure the list is correctly typed to List<GameMode>
            Type gameModeListType = new com.google.gson.reflect.TypeToken<List<GameMode>>() {}.getType();
            List<GameMode> games = context.deserialize(jsonObject.get("games"), gameModeListType);
            
            // 确保反序列化后的游戏列表非空
            if (games == null || games.isEmpty()) {
                System.out.println("警告: 从保存文件恢复的游戏列表为空，将创建默认游戏。");
                GameList.validateGamesInitialized(); // 使用validateGamesInitialized来初始化游戏
            } else {
                // 设置游戏列表
                GameList.setGames(games);
                System.out.println("成功加载游戏列表，包含 " + games.size() + " 个游戏模式");
            }
        } catch (Exception e) {
            System.err.println("反序列化GameList时出错: " + e.getMessage());
            e.printStackTrace();
            // 确保游戏列表有效
            GameList.validateGamesInitialized();
        } finally {
            GameList.endDeserialization(); // Signal end of deserialization
        }
        
        // 使用专门的反序列化构造函数创建实例
        return new GameList(true); 
    }
}
