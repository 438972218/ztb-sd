package com.xdcplus.ztb.common.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * redis 配置
 * @author Rong.Jia
 * @date 2019/01/14 17:20
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
//@ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
public class RedisConfig extends CachingConfigurerSupport {

    private Duration timeToLive = Duration.ofSeconds(60);

    private LettuceConnectionFactory lettuceConnectionFactory;
	private CacheProperties properties;

    public RedisConfig(LettuceConnectionFactory lettuceConnectionFactory, CacheProperties properties) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
        this.properties = properties;
    }

    /**
     * 在没有指定缓存Key的情况下，key生成策略
     * @date 2019/04/22 14:02:22
     * @return KeyGenerator
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     *  缓存管理器 使用Lettuce，和jedis有很大不同
     * @date 2019/04/22 14:02:22
     * @return CacheManager 缓存管理器
     */
    @Bean
    @Primary
    @Override
    public CacheManager cacheManager() {

        //关键点，com.xdcplus.spring cache的注解使用的序列化都从这来，没有这个配置的话使用的jdk自己的序列化，实际上不影响使用，只是打印出来不适合人眼识别
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()

                //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))

                //value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))

                .disableCachingNullValues()

                //缓存过期时间
                .entryTtl((properties.getRedis() == null
                        || properties.getRedis().getTimeToLive() == null
                        || properties.getRedis().getTimeToLive().isZero())
                        ? timeToLive : properties.getRedis().getTimeToLive());

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(config)
                .transactionAware();

        return builder.build();
    }

    /**
     *  RedisTemplate配置 在单独使用redisTemplate的时候 重新定义序列化方式
     * @date 2019/04/22 14:02:22
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(valueSerializer());
        template.setHashValueSerializer(valueSerializer());

        template.setHashKeySerializer(keySerializer());

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(keySerializer());

        template.setConnectionFactory(lettuceConnectionFactory);
        template.afterPropertiesSet();

        return template;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {

        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        //序列化时允许非常量字段均输出类型
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return jackson2JsonRedisSerializer;

        //或者使用GenericJackson2JsonRedisSerializer
        //return new GenericJackson2JsonRedisSerializer();
    }

    /**
     *  注入封装RedisTemplate
     * @author Rong.Jia
     * @date 2019/01/14 17:20
     * @return RedisUtils
     */
    @Bean(name = "redisUtils")
    public RedisUtils redisUtils(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setRedisTemplate(redisTemplate);
        return redisUtils;
    }

}
