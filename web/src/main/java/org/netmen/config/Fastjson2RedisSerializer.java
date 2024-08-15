package org.netmen.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

//使用fastjson2实现RedisSerializer接口
public class Fastjson2RedisSerializer<T> implements RedisSerializer<T> {
    private Class<T> clazz;

    public Fastjson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    //序列化
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes();
    }

    //反序列化
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes);
        return JSON.parseObject(str, clazz);
    }
}
