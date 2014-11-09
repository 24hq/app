package app.infrastructure.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfiguration {

    @Bean
    RedisConnectionFactory connectionFactory() {
        def connectionFactory = new JedisConnectionFactory()
        connectionFactory.usePool = true
        connectionFactory
    }

    @Bean
    @Scope("prototype")
    RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        def redisTemplate = new RedisTemplate()
        redisTemplate.connectionFactory = connectionFactory
        redisTemplate
    }

}
