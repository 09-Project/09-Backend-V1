package com.example.project09.security.jwt;

import com.example.project09.exception.InvalidTokenException;
import com.example.project09.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.exp.acc}")
    private Long accessTokenExpiration;

    @Value("${jwt.exp.ref}")
    private Long refreshTokenExpiration;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    private final CustomUserDetailsService customUserDetailsService;

    protected String init() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "access")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, init())
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, init())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUsername(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims getUsername(String token) {
        return Jwts.parser().setSigningKey(init()).parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(header);

        if(StringUtils.hasText(token) && token.startsWith(prefix)) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            return !getUsername(token)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean isRefreshToken(String token) {
        return getUsername(token).get("type").equals("refresh");
    }

}
