package com.example.project09.service.member;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.exception.InvalidPasswordException;
import com.example.project09.exception.UserNotFoundException;
import com.example.project09.payload.member.request.InformationRequest;
import com.example.project09.payload.member.request.PasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(PasswordRequest request, Member member) {
        checkPassword(request.getPassword(), member.getUsername());
        memberRepository.findByUsername(member.getUsername())
                .map(password -> memberRepository.save(
                        password.updatePassword(passwordEncoder.encode(request.getNewPassword()))
                ))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateInfo(InformationRequest request, Member member) {
        memberRepository.findByUsername(member.getUsername())
                .map(info -> memberRepository.save(
                        info.updateInfo(request.getName(), request.getIntroduction())
                ))
                .orElseThrow(UserNotFoundException::new);
    }

    public void checkPassword(String password, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(password, member.getPassword())) throw new InvalidPasswordException();
    }

}
