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
import com.example.project09.exception.*;
import com.example.project09.facade.MemberFacade;
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
import com.example.project09.service.image.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${jwt.exp.refresh}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        if(memberRepository.findByName(request.getName()).isPresent())
            throw new UserAlreadyExistsException();
        else if(memberRepository.findByUsername(request.getUsername()).isPresent())
            throw new UserAlreadyExistsException();

        memberRepository.save(Member.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .everyLikeCounts(0)
                .role(Role.ROLE_USER)
                .build());
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

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
    public void updatePassword(UpdatePasswordRequest request) {
        checkPassword(request.getPassword(), MemberFacade.getMemberId());

        memberRepository.findById(MemberFacade.getMemberId())
                .map(password -> memberRepository.save(
                        password.updatePassword(passwordEncoder.encode(request.getNewPassword()))
                ))
                .orElseThrow(UserNotFoundException::new);
    }

    public void checkPassword(String password, Integer id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new InvalidPasswordException();
    }

    @Override
    @Transactional
    public void updateInfo(UpdateInformationRequest request) {
        if(memberRepository.findByName(request.getName()).isPresent())
            throw new UserAlreadyExistsException();

        memberRepository.findById(MemberFacade.getMemberId())
                .map(info -> info.updateInfo(request.getName(), request.getIntroduction(),
                        s3Service.getFileUrl(s3Service.upload(request.getProfile(), "static")))
                )
                .orElseThrow(UserNotFoundException::new);
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
                                                .image(imageRepository.findByPostId(post.getId())
                                                        .map(Image::getImageUrl).orElseThrow(ImageNotFoundException::new))
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
    public MemberMyPageResponse getMyPage() {
        MemberProfileResponse memberProfileResponse = getMemberProfile(MemberFacade.getMemberId());
        
        List<MemberLikePostsResponse.likePosts> likePosts = likeRepository.findByMemberId(MemberFacade.getMemberId())
                .stream()
                .map(like -> {
                    MemberLikePostsResponse.likePosts response = MemberLikePostsResponse.likePosts.builder()
                            .title(like.getPost().getTitle())
                            .image(like.getPost().getImage().getImageUrl())
                            .build();
                    return response;
                })
                .collect(Collectors.toList());

        return new MemberMyPageResponse(memberProfileResponse,
                new MemberLikePostsResponse(likeRepository.countByMemberId(MemberFacade.getMemberId()), likePosts));
    }

}
