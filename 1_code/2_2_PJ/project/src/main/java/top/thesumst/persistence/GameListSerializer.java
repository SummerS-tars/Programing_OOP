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
    }

    @Override
    public GameList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        GameList.startDeserialization(); // Signal start of deserialization

        JsonObject jsonObject = json.getAsJsonObject();

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
            // 创建默认的GameList实例以确保有游戏
            GameList.endDeserialization(); // 先结束反序列化状态
            return new GameList(); // 返回一个带有默认游戏的实例
        }
        
        GameList.setGames(games);
        GameList.endDeserialization(); // Signal end of deserialization
        
        // 返回一个可能为空的实例，但静态的games已经被设置
        return new GameList(); 
    }
}
