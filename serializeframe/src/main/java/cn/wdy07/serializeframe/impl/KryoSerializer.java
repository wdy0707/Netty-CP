package cn.wdy07.serializeframe.impl;

import cn.wdy07.serializeframe.intf.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wdy
 * @create 2020-06-20 23:05
 */
public class KryoSerializer implements Serializer {

    private final Class<?> clazz;

    public KryoSerializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    //保证Kryo线程安全，每个线程单独使用
    private final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(clazz,new BeanSerializer(kryo,clazz));
            return kryo;
        }
    };

    private Class<?> getClazz(){
        return clazz;
    }

    private Kryo getKryo(){
        return kryoThreadLocal.get();
    }


    @Override
    public byte[] serialize(Object obj) {
        //缓存作用
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Output output = new Output(byteArray);

        Kryo kryo = getKryo();
        kryo.writeObjectOrNull(output,obj,obj.getClass());
        output.flush();//待优化(AOP)
        output.close();

        byte[] objBytes = byteArray.toByteArray();
        try {
            byteArray.flush();
            byteArray.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objBytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        if(bytes == null) return null;

        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArray);
        Kryo kryo = getKryo();
        T tObj = (T) kryo.readObjectOrNull(input, getClazz());
        input.close();
        try {
            byteArray.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tObj;
    }
}
