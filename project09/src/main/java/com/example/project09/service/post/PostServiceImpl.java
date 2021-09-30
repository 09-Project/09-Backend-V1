package com.example.project09.service.post;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.Like;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.post.Post;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.entity.post.Purpose;
import com.example.project09.exception.ImageNotFoundException;
import com.example.project09.exception.LikeAlreadyExistsException;
import com.example.project09.exception.LikeNotFoundException;
import com.example.project09.exception.PostNotFoundException;
import com.example.project09.facade.MemberFacade;
import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.OtherPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final ImageService imageService;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void createPost(PostRequest request) throws IOException {
        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .transactionRegion(request.getTransactionRegion())
                .openChatLink(request.getOpenChatLink())
                .purpose(Purpose.CO_PURCHASE)
                .member(MemberFacade.getMember())
                .likeCounts(0)
                .build());

        imageService.uploadFile(request.getImage(), post);
        setPurpose(post);
    }

    @Transactional
    public void setPurpose(Post post) {
        if(post.getPrice() == null)
            post.updatePurpose(Purpose.DONATION);
    }

    @Override
    @Transactional
    public void modifyPost(PostRequest request, Integer id) throws IOException {
        Post post = postRepository.findById(id)
                .map(newPost -> newPost.updatePost(
                        request.getTitle(),
                        request.getContent(),
                        request.getPrice(),
                        request.getTransactionRegion(),
                        request.getOpenChatLink()
                ))
                .orElseThrow(PostNotFoundException::new);
        imageService.updateFile(request.getImage(), post);
    }

    @Override
    @Transactional
    public void removePost(Integer id) {
        imageService.removeFile(id);
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(Pageable pageable) {
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
                            .image(imageRepository.findByPostId(post.getId())
                                    .map(Image::getImageUrl).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return response;
                })
                .sorted(Comparator.comparing(PostResponse::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EachPostResponse getEachPost(Integer id) { // 다른 상품 추천 추가
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
                            .image(imageRepository.findByPostId(post.getId())
                                    .map(Image::getImageUrl).orElseThrow(ImageNotFoundException::new))
                            .getLikes(post.getLikeCounts())
                            .memberName(post.getMember().getName())
                            .memberIntroduction(post.getMember().getIntroduction())
                            .postsCount(postRepository.countByMemberId(MemberFacade.getMemberId()))
                            .everyLikeCounts(post.getMember().getEveryLikeCounts())
                            .build();
                    return response;
                })
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherPostResponse> getOtherPosts() {
        return postRepository.otherPosts()
                .stream()
                .map(post -> {
                    OtherPostResponse response = OtherPostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .image(post.getImage().getImageUrl())
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addLike(Integer id) {
        if(likeRepository.findByMemberIdAndPostId(MemberFacade.getMemberId(), id).isPresent())
            throw new LikeAlreadyExistsException();

        likeRepository.save(
                Like.builder()
                        .post(postRepository.findById(id).orElseThrow(PostNotFoundException::new))
                        .member(MemberFacade.getMember())
                        .build());

        postRepository.findById(id)
                .map(post -> {
                    post.getMember().addLikeCounts();
                    post.addPostLikeCounts();
                    return post;
                })
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public void removeLike(Integer id) {
        likeRepository.delete(
                likeRepository.findByMemberIdAndPostId(MemberFacade.getMemberId(), id).orElseThrow(LikeNotFoundException::new)
        );
        postRepository.findById(id)
                .map(post -> post.getMember().removeLikeCounts())
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public void removeAllLikes() {
        likeRepository.deleteByMemberId(MemberFacade.getMemberId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword, Pageable pageable) { // 키워드 오류 수정
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
                            .image(imageRepository.findByPostId(post.getId())
                                    .map(Image::getImagePath).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return response;
                })
                .sorted(Comparator.comparing(PostResponse::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

}
