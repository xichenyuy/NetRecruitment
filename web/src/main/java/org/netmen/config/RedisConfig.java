package org.netmen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    @SuppressWarnings(value = {"uncheck", "rawtypes"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用StringRedisSerializer来序列化和反序列化key值
        template.setKeySerializer(new StringRedisSerializer());

        // 使用自定义的Fastjson2RedisSerializer来序列化和反序列化value值
        template.setValueSerializer(new Fastjson2RedisSerializer<>(Object.class));

        // Hash的序列化和反序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Fastjson2RedisSerializer<>(Object.class));

        //其他配置...

        template.afterPropertiesSet();
        return template;
    }
}
