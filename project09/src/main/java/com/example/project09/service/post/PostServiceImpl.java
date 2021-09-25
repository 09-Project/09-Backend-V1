package com.example.project09.service.post;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.like.Like;
import com.example.project09.entity.like.LikeRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.post.Post;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.entity.post.Purpose;
import com.example.project09.exception.ImageNotFoundException;
import com.example.project09.exception.LikeAlreadyExistsException;
import com.example.project09.exception.LikeNotFoundException;
import com.example.project09.exception.PostNotFoundException;
import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Value("${img.path}")
    private String IMAGE_PATH;

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void createPost(PostRequest request, Member member) throws IOException {
        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .transactionRegion(request.getTransactionRegion())
                .openChatLink(request.getOpenChatLink())
                .purpose(Purpose.CO_PURCHASE)
                .member(member)
                .likeCounts(0)
                .build());

        if(post.getPrice() == null)
            postRepository.save(post.updatePurpose(Purpose.DONATION));

        MultipartFile file = request.getImage();
        UUID uuid = UUID.randomUUID();
        Image image = imageRepository.save(Image.builder()
                .image(uuid + "_" + file.getOriginalFilename())
                .post(post)
                .build());
        file.transferTo(new File(IMAGE_PATH + image.getImage()));

    }

    @Override
    @Transactional
    public void modifyPost(PostRequest request, Integer id) throws IOException {
        postRepository.findById(id)
                .map(newPost -> newPost.updatePost(
                        request.getTitle(),
                        request.getContent(),
                        request.getPrice(),
                        request.getTransactionRegion(),
                        request.getOpenChatLink()
                ))
                .orElseThrow(PostNotFoundException::new);

        MultipartFile file = request.getImage();
        UUID uuid = UUID.randomUUID();
        Image image = imageRepository.findByPostId(id)
                .map(newImage -> newImage.updateImage(uuid + "_" + file.getOriginalFilename()))
                .orElseThrow(ImageNotFoundException::new);
        file.transferTo(new File(IMAGE_PATH + image.getImage()));

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
                                    .map(Image::getImage).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return response;
                })
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
                                    .map(Image::getImage).orElseThrow(ImageNotFoundException::new))
                            .member(post.getMember())
                            .getLikes(post.getLikeCounts())
                            .build();
                    return response;
                })
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public void addLike(Integer id, Member member) {
        if(likeRepository.existsByMemberIdAndPostId(member.getId(), id))
            throw new LikeAlreadyExistsException();

        likeRepository.save(
                Like.builder()
                        .post(postRepository.findById(id).orElseThrow(PostNotFoundException::new))
                        .member(member)
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
    public void removeLike(Integer id, Member member) {
        likeRepository.delete(
                likeRepository.findByMemberAndPostId(member, id).orElseThrow(LikeNotFoundException::new)
        );
        postRepository.findById(id)
                .map(post -> post.getMember().removeLikeCounts())
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public void removeAllLikes(Member member) {
        likeRepository.deleteByMemberId(member.getId());
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
                                    .map(Image::getImage).orElseThrow(ImageNotFoundException::new))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }


}
