package com.example.project09.service.auth;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.member.Role;
import com.example.project09.entity.refreshtoken.RefreshToken;
import com.example.project09.entity.refreshtoken.RefreshTokenRepository;
import com.example.project09.exception.*;
import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.exp.refresh}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        if(memberRepository.existsByNameOrUsername(request.getName(), request.getUsername()))
            throw new UserAlreadyExistsException();

        Member member = Member.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .everyLikeCounts(0)
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new InvalidPasswordException();

        return createToken(request.getUsername());
    }

    @Override
    @Transactional
    public TokenResponse reissue(String token) {
        if(!tokenProvider.validateToken(token) || !tokenProvider.isRefreshToken(token))
            throw new InvalidTokenException();

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .map(refresh -> refreshTokenRepository.save(
                        refresh.update(REFRESH_TOKEN_EXPIRATION_TIME)
                ))
                .orElseThrow(InvalidTokenException::new);

        return new TokenResponse(tokenProvider.createAccessToken(refreshToken.getUsername()), token);
    }

    @Transactional
    public TokenResponse createToken(String username) {
        String accessToken = tokenProvider.createAccessToken(username);
        String refreshToken = tokenProvider.createRefreshToken(username);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .refreshExpiration(REFRESH_TOKEN_EXPIRATION_TIME)
                        .build());

        return new TokenResponse(accessToken, refreshToken);
    }

}
