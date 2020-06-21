package cn.wdy07.serializeframe.intf;

/**
 * @author wdy
 * @create 2020-06-20 22:52
 */
public interface Serializer {

    //序列化
    byte[] serialize(Object obj);

    //反序列化
    <T> T deserialize(byte[] bytes);
}
