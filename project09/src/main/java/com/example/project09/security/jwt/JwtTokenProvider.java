package com.example.project09.security.jwt;

import com.example.project09.exception.ExpiredAccessTokenException;
import com.example.project09.exception.ExpiredRefreshTokenException;
import com.example.project09.exception.IncorrectTokenException;
import com.example.project09.exception.InvalidTokenException;
import com.example.project09.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private String SECRET_KEY;

    @Value("${jwt.header}")
    private String HEADER;

    @Value("${jwt.prefix}")
    private String PREFIX;

    @Value("${jwt.exp.access}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.exp.refresh}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setSubject(username)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUsername(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);

        if(StringUtils.hasText(token) && token.startsWith(PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    public Claims getUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new IncorrectTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean validateToken(String token) {
        return !getUsername(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean isRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            return claims.get("type").equals("refresh") && !claims.getExpiration().before(new Date());
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new IncorrectTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

}
