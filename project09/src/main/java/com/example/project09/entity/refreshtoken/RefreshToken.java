package com.example.project09.entity.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    private String username;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long refreshExpiration;

    public RefreshToken update(Long refreshExp) {
        this.refreshExpiration = refreshExp;
        return this;
    }

}
