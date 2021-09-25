package com.example.project09.service.member;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.member.Role;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.entity.refreshtoken.RefreshToken;
import com.example.project09.entity.refreshtoken.RefreshTokenRepository;
import com.example.project09.exception.InvalidPasswordException;
import com.example.project09.exception.InvalidTokenException;
import com.example.project09.exception.UserAlreadyExistsException;
import com.example.project09.exception.UserNotFoundException;
import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberLikePostsResponse;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${jwt.exp.refresh}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider tokenProvider;
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
                .map(refresh -> refresh.update(REFRESH_TOKEN_EXPIRATION_TIME))
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
    public void updateInfo(UpdateInformationRequest request, Member member) throws IOException {
        if(memberRepository.existsByName(request.getName()))
            throw new UserAlreadyExistsException();

        UUID uuid = UUID.randomUUID();
        memberRepository.findByUsername(member.getUsername())
                .map(info -> info.updateInfo(request.getName(), request.getIntroduction(),
                        uuid + "_" + request.getProfile().getOriginalFilename())
                )
                .orElseThrow(UserNotFoundException::new);

        request.getProfile().transferTo(
                new File("C:\\Users\\user\\Desktop\\" + uuid + "_" + request.getProfile().getOriginalFilename())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Integer id) {
        Integer everyLikeCounts = memberRepository.findById(id).orElseThrow().getEveryLikeCounts();

        return memberRepository.findById(id)
                .map(member -> {
                    MemberProfileResponse memberProfileResponse = MemberProfileResponse.builder()
                            .name(member.getName())
                            .profileUrl(member.getProfileUrl())
                            .introduction(member.getIntroduction())
                            .posts(postRepository.findByMemberId(id)
                                    .stream().map(post -> {
                                        PostResponse postResponse = PostResponse.builder()
                                                .id(post.getId())
                                                .title(post.getTitle())
                                                .price(post.getPrice())
                                                .transactionRegion(post.getTransactionRegion())
                                                .purpose(post.getPurpose())
                                                .createdDate(post.getCreatedDate())
                                                .updatedDate(post.getUpdatedDate())
                                                .images(imageRepository.findAllByPostId(post.getId()) // 대표 이미지 설정
                                                        .stream().map(Image::getImages).collect(Collectors.toList()))
                                                .build();
                                        return postResponse;
                                    }).collect(Collectors.toList()))
                            .postsCount(postRepository.countByMemberId(id))
                            .getLikesCount(everyLikeCounts)
                            .build();
                    return memberProfileResponse;
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberMyPageResponse getMyPage(Member member) {
        MemberProfileResponse memberProfileResponse = getMemberProfile(member.getId());

        Integer likePostsCount = likeRepository.countByMemberId(member.getId());
        
        List<MemberLikePostsResponse.likePosts> likePosts = likeRepository.findByMemberId(member.getId())
                .stream()
                .map(like -> {
                    MemberLikePostsResponse.likePosts response = MemberLikePostsResponse.likePosts.builder()
                            .title(like.getPost().getTitle())
                            .images(like.getPost().getImages()
                                    .stream().map(image -> image.getImages())
                                    .collect(Collectors.toList()))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());

        return new MemberMyPageResponse(memberProfileResponse, new MemberLikePostsResponse(likePostsCount, likePosts));
    }

    public void checkPassword(String password, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new InvalidPasswordException();
    }

}
