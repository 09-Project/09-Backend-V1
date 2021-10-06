package com.example.project09.service.member;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.member.Role;
import com.example.project09.entity.post.Completed;
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
import com.example.project09.payload.member.response.MemberInfoResponse;
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
            throw new MemberNameAlreadyExistsException();
        else if(memberRepository.findByUsername(request.getUsername()).isPresent())
            throw new MemberUsernameAlreadyExistsException();

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
                .orElseThrow(MemberNotFoundException::new);

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new InvalidPasswordException();

        return createToken(request.getUsername());
    }

    @Override
    @Transactional
    public TokenResponse reissue(String refreshToken) {
        tokenProvider.isRefreshToken(refreshToken);
        RefreshToken newRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(refresh -> refreshTokenRepository.save(
                        refresh.update(REFRESH_TOKEN_EXPIRATION_TIME)
                ))
                .orElseThrow(RefreshTokenNotFoundException::new);

        return new TokenResponse(tokenProvider.createAccessToken(newRefreshToken.getUsername()), refreshToken);
    }

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {
        checkPassword(request.getPassword());
        memberRepository.findById(MemberFacade.getMemberId())
                .map(password -> password.updatePassword(passwordEncoder.encode(request.getNewPassword())))
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateInfo(UpdateInformationRequest request) {
        if(request.getName() != MemberFacade.getMember().getName()
                && memberRepository.findByName(request.getName()).isPresent())
            throw new MemberNameAlreadyExistsException();

        if(MemberFacade.getMember().getProfileUrl() != null)
            s3Service.removeFile(MemberFacade.getMember().getProfileUrl().substring(55));

        memberRepository.findById(MemberFacade.getMemberId())
                .map(info -> info.updateInfo(request.getName(), request.getIntroduction(),
                        s3Service.getFileUrl(s3Service.upload(request.getProfileUrl(), "static")))
                )
                .orElseThrow(MemberNotFoundException::new);
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
                            .posts(getMemberPosts())
                            .postsCount(postRepository.countByMemberId(id))
                            .getLikesCount(memberRepository.findById(id).get().getEveryLikeCounts())
                            .inProgressPostsCount(postRepository.countByMemberIdAndCompleted(id, Completed.IN_PROGRESS))
                            .completedPostsCount(postRepository.countByMemberIdAndCompleted(id, Completed.COMPLETED))
                            .build();
                    return memberProfileResponse;
                })
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberMyPageResponse getMyPage() {
        return new MemberMyPageResponse(getMemberProfile(MemberFacade.getMemberId()),
                new MemberLikePostsResponse(likePostCounts(MemberFacade.getMemberId()), getMemberLikePosts()));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo() {
        return memberRepository.findById(MemberFacade.getMemberId())
                .map(member -> {
                    MemberInfoResponse response = MemberInfoResponse.builder()
                            .name(member.getName())
                            .profileUrl(member.getProfileUrl())
                            .introduction(member.getIntroduction())
                            .build();
                    return response;
                })
                .orElseThrow(MemberNotFoundException::new);
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

    public void checkPassword(String password) {
        Member member = memberRepository.findById(MemberFacade.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new InvalidPasswordException();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMemberPosts() {
        return postRepository.findByMemberId(MemberFacade.getMemberId())
                .stream()
                .map(post -> {
                    PostResponse postResponse = PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .purpose(post.getPurpose())
                            .completed(post.getCompleted())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .image(imageRepository.findByPostId(post.getId())
                                    .map(Image::getImageUrl).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return postResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMemberLikePosts() {
        return likeRepository.findByMemberId(MemberFacade.getMemberId())
                .stream()
                .map(like -> {
                    PostResponse response = PostResponse.builder()
                            .id(like.getId())
                            .title(like.getPost().getTitle())
                            .price(like.getPost().getPrice())
                            .transactionRegion(like.getPost().getTransactionRegion())
                            .purpose(like.getPost().getPurpose())
                            .createdDate(like.getPost().getCreatedDate())
                            .updatedDate(like.getPost().getUpdatedDate())
                            .image(imageRepository.findByPostId(like.getPost().getId())
                                    .map(Image::getImageUrl).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer likePostCounts(Integer memberId) {
        return likeRepository.countByMemberId(memberId);
    }

}
