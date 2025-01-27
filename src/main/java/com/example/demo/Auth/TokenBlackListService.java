package com.example.demo.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_KEY_PREFIX = "blacklisted_token:";

    public void blacklistToken(String token, long expirationTimeMillis) {
        long expirationInSeconds = expirationTimeMillis / 1000;
        redisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + token, "true", expirationInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + token);
    }
}
