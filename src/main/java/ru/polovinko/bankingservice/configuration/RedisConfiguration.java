package ru.polovinko.bankingservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import ru.polovinko.bankingservice.entity.RefreshToken;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(keyspaceConfiguration = RedisConfiguration.RRefreshTokenKeyConfiguration.class,
  enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfiguration {
  @Value("${app.jwt.refreshTokenExpiration}")
  private Duration refreshTokenExpiration;

  @Bean
  public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
    var configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisProperties.getHost());
    configuration.setPort(redisProperties.getPort());
    return new JedisConnectionFactory(configuration);
  }

  public class RRefreshTokenKeyConfiguration extends KeyspaceConfiguration {
    private static final String REFRESH_TOKEN_KEYSPACE = "refresh tokens";

    protected Iterable<KeyspaceSettings> initialConfiguration() {
      var keyspaceSettings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);
      keyspaceSettings.setTimeToLive(refreshTokenExpiration.getSeconds());
      return Collections.singleton(keyspaceSettings);
    }
  }
}
