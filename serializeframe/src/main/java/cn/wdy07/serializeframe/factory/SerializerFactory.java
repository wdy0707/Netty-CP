package cn.wdy07.serializeframe.factory;

import cn.wdy07.serializeframe.impl.KryoSerializer;
import cn.wdy07.serializeframe.intf.Serializer;

/**
 * @author wdy
 * @create 2020-06-20 23:36
 */
public class SerializerFactory {

    public static Serializer getSerizalizer(Class<?> clazz){
        return new KryoSerializer(clazz);
    }
}
