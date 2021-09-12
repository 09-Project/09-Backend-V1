package com.example.project09.service.post;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.member.Member;
import com.example.project09.entity.post.Post;
import com.example.project09.entity.post.PostRepository;
import com.example.project09.entity.post.Purpose;
import com.example.project09.exception.PostNotFoundException;
import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void createPost(PostRequest request, Member member) {
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

        for (MultipartFile file : request.getMultipartFiles()) {
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

        for (MultipartFile file : request.getMultipartFiles()) {
            imageRepository.save(
                    imageRepository.findByPostId(id)
                            .map(image -> image.updateProfileUrl(file.getOriginalFilename()))
                            .orElseThrow(PostNotFoundException::new)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> {
                    PostResponse response = PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .openChatLink(post.getOpenChatLink())
                            .purpose(post.getPurpose())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .images(imageRepository.findAllByPostId(post.getId())
                                    .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getMemberPosts(Member member) {
        return postRepository.findByMemberId(member.getId())
                .stream()
                .map(post -> {
                    PostResponse response = PostResponse.builder()
                            .id(post.getId())
                            .name(post.getMember().getName())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .price(post.getPrice())
                            .transactionRegion(post.getTransactionRegion())
                            .openChatLink(post.getOpenChatLink())
                            .purpose(post.getPurpose())
                            .createdDate(post.getCreatedDate())
                            .updatedDate(post.getUpdatedDate())
                            .images(imageRepository.findAllByPostId(post.getId())
                                    .stream().map(Image::getProfileUrl).collect(Collectors.toList()))
                            .build();
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EachPostResponse getEachPost(Integer id) {
        Post postDetails = postRepository.findById(id)
                .map(post -> {
                    post.getMember().getName();
                    post.getTitle();
                    post.getContent();
                    post.getPrice();
                    post.getTransactionRegion();
                    post.getOpenChatLink();
                    post.getPurpose();
                    post.getCreatedDate();
                    post.getUpdatedDate();
                    post.getImages();
                    post.getMember();
                    return post;
                })
                .orElseThrow(PostNotFoundException::new);

        return new EachPostResponse(postDetails);
    }


}
