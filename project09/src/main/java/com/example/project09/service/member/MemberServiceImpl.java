package com.example.project09.service.member;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.exception.InvalidPasswordException;
import com.example.project09.exception.UserAlreadyExistsException;
import com.example.project09.exception.UserNotFoundException;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.payload.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request, Member member) {
        checkPassword(request.getPassword(), member.getUsername());
        memberRepository.findByUsername(member.getUsername())
                .map(password -> memberRepository.save(
                        password.updatePassword(passwordEncoder.encode(request.getNewPassword()))
                ))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateInfo(UpdateInformationRequest request, Member member) {
        if(memberRepository.existsByName(request.getName()))
            throw new UserAlreadyExistsException();

        memberRepository.findByUsername(member.getUsername())
                .map(info -> memberRepository.save(
                        info.updateInfo(request.getName(), request.getIntroduction(),
                                request.getProfile().getOriginalFilename())
                ))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Integer id) {
        return memberRepository.findById(id)
                .map(member -> {
                    MemberProfileResponse memberProfileResponse = MemberProfileResponse.builder()
                            .name(member.getName())
                            .profileUrl(member.getProfileUrl())
                            .introduction(member.getIntroduction())
                            .posts(postRepository.findByMemberId(id)
                                    .stream()
                                    .map(post -> {
                                        PostResponse postResponse = PostResponse.builder()
                                                .id(post.getId())
                                                .title(post.getTitle())
                                                .price(post.getPrice())
                                                .transactionRegion(post.getTransactionRegion())
                                                .purpose(post.getPurpose())
                                                .createdDate(post.getCreatedDate())
                                                .updatedDate(post.getUpdatedDate())
                                                .images(imageRepository.findAllByPostId(post.getId())
                                                        .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                                                .build();
                                        return postResponse;
                                    }).collect(Collectors.toList()))
                            .build();
                    return memberProfileResponse;
                })
                .orElseThrow();
    }

    public void checkPassword(String password, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new InvalidPasswordException();
    }

}
