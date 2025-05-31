package top.thesumst.persistence;

/**
 * 可序列化接口
 * 为复杂对象提供自定义序列化支持
 */
public interface Serializable {
    
    /**
     * 将对象序列化为JSON字符串
     * @return JSON格式的字符串表示
     */
    String toJson();
    
    /**
     * 从JSON字符串恢复对象状态
     * @param json JSON格式的字符串
     */
    void fromJson(String json);
    
    /**
     * 获取序列化版本号
     * 用于处理版本兼容性问题
     * @return 版本号
     */
    default String getSerializationVersion() {
        return "1.0";
    }
    
    /**
     * 验证反序列化后的对象状态是否有效
     * @return 如果对象状态有效返回true，否则返回false
     */
    default boolean validateAfterDeserialization() {
        return true;
    }
}
