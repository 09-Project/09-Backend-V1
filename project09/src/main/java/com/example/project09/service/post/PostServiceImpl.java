package com.example.project09.service.post;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.Like;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.member.MemberRepository;
import com.example.project09.entity.post.Post;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.entity.post.Purpose;
import com.example.project09.exception.LikeNotFoundException;
import com.example.project09.exception.PostNotFoundException;
import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void createPost(PostRequest request, Member member) { // 대표 이미지 설정
        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .transactionRegion(request.getTransactionRegion())
                .openChatLink(request.getOpenChatLink())
                .purpose(Purpose.CO_PURCHASE)
                .member(member)
                .build());

        if(post.getPrice() == null)
            postRepository.save(post.updatePurpose(Purpose.DONATION));

        for (MultipartFile file : request.getImages()) {
            imageRepository.save(Image.builder()
                    .profileUrl(file.getOriginalFilename())
                    .post(post)
                    .build());
        }
    }

    @Override
    @Transactional
    public void modifyPost(PostRequest request, Integer id) {
        postRepository.save(
                postRepository.findById(id)
                        .map(post -> post.updatePost(
                                request.getTitle(),
                                request.getContent(),
                                request.getPrice(),
                                request.getTransactionRegion(),
                                request.getOpenChatLink()
                        ))
                        .orElseThrow(PostNotFoundException::new)
        );

        for (MultipartFile file : request.getImages()) {
            imageRepository.save(
                    imageRepository.findByPostId(id)
                            .map(image -> image.updateProfileUrl(file.getOriginalFilename()))
                            .orElseThrow(PostNotFoundException::new)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(Pageable pageable) { // 페이징 처리
        return postRepository.findAll(pageable)
                .stream()
                .map(post -> {
                    PostResponse response = PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .purpose(post.getPurpose())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .images(imageRepository.findAllByPostId(post.getId()) // 대표 이미지 설정
                                    .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EachPostResponse getEachPost(Integer id) { // 좋아요 기능, 다른 상품 보기 추가
        return postRepository.findById(id)
                .map(post -> {
                    EachPostResponse response = EachPostResponse.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .openChatLink(post.getOpenChatLink())
                            .purpose(post.getPurpose())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .images(imageRepository.findAllByPostId(post.getId()) // 대표 이미지 설정
                                    .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                            .member(post.getMember())
                            .build();
                    return response;
                })
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public void addLike(Integer id, Member member) {
        likeRepository.save(
                Like.builder()
                        .post(postRepository.findById(id).orElseThrow(PostNotFoundException::new))
                        .member(member)
                        .build());
    }

    @Override
    @Transactional
    public void removeLike(Integer id, Member member) {
        likeRepository.delete(
                likeRepository.findByMemberAndPostId(member, id).orElseThrow(LikeNotFoundException::new)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable)
                .stream()
                .map(post -> {
                    PostResponse response = PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .purpose(post.getPurpose())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .images(imageRepository.findAllByPostId(post.getId()) // 대표 이미지 설정
                                    .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }


}
