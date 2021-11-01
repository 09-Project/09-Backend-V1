package com.example.project09.service.member;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.post.Completed;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.exception.ImageNotFoundException;
import com.example.project09.exception.InvalidPasswordException;
import com.example.project09.exception.MemberNameAlreadyExistsException;
import com.example.project09.exception.MemberNotFoundException;
import com.example.project09.facade.MemberFacade;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.image.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

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
        if(!request.getName().equals(MemberFacade.getMemberName())
                && memberRepository.findByName(request.getName()).isPresent())
            throw new MemberNameAlreadyExistsException();

        if(MemberFacade.getMemberProfileUrl() != null)
            s3Service.removeFile(MemberFacade.getMemberProfileUrl().substring(55));

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
                            .allPostsCount(postRepository.countByMemberId(id))
                            .getLikesCount(memberRepository.findById(id).get().getEveryLikeCounts())
                            .inProgressPostsCount(postRepository.countByMemberIdAndCompleted(id, Completed.IN_PROGRESS))
                            .completedPostsCount(postRepository.countByMemberIdAndCompleted(id, Completed.COMPLETED))
                            .likePostsCount(likeRepository.countByMemberId(member.getId()))
                            .build();
                    return memberProfileResponse;
                })
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMyPage() {
        return getMemberProfile(MemberFacade.getMemberId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getMemberInProgressPosts(Integer id) {
        return postRepository.findByMemberId(id)
                .stream()
                .filter(post -> post.getCompleted() == Completed.IN_PROGRESS)
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
                .sorted(Comparator.comparing(PostResponse::getUpdatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getMemberCompletedPosts(Integer id) {
        return postRepository.findByMemberId(id)
                .stream()
                .filter(post -> post.getCompleted() == Completed.COMPLETED)
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
                .sorted(Comparator.comparing(PostResponse::getUpdatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
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
                .sorted(Comparator.comparing(PostResponse::getUpdatedDate).reversed())
                .collect(Collectors.toList());
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

    public void checkPassword(String password) {
        Member member = memberRepository.findById(MemberFacade.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new InvalidPasswordException();
    }

}
