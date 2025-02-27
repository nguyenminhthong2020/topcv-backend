package com.example.Job.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import java.time.Duration;

@Configuration
@EnableCaching // Bật tính năng caching
public class RedisCacheConfig {

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)) // Thời gian sống mặc định của cache
                                .disableCachingNullValues(); // Không cache giá trị null

                return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(defaultConfig)
                                // .withCacheConfiguration("products",
                                // RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                                // // TTL riêng cho
                                // // cache
                                // // "products"
                                .build();
        }
}
