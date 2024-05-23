package ru.polovinko.bankingservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import ru.polovinko.bankingservice.entity.RefreshToken;

@Component
@Slf4j
public class RedisExpirationEvent {
  @EventListener
  public void handleRedisExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
    var expiredRefreshToken = (RefreshToken) event.getValue();
    if (expiredRefreshToken == null) {
      throw new RuntimeException("Refresh token is null in handleRedisKeyExpiredEvent function!");
    }
    log.info("Refresh token with key ={} has expired! Refresh token is: {}", expiredRefreshToken.getId(),
      expiredRefreshToken.getToken());
  }
}
