package com.example.project09.service.auth;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.member.Role;
import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.AccessTokenResponse;
import com.example.project09.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequest request) {
        if(memberRepository.existsByNameOrUsername(request.getName(), request.getUsername())) {
            throw new IllegalArgumentException();
        }

        Member member = Member.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .introduction(request.getIntroduction())
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    @Override
    public AccessTokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException());

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException();
        }

        return tokenProvider.createAccessToken(member.getUsername());
    }

}
