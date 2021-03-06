package com.example.project09.service.post;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.OtherPostResponse;
import com.example.project09.payload.post.response.PostResultResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {
    void createPost(PostRequest request);
    void modifyPost(PostRequest request, Integer id);
    void removePost(Integer id);
    void addLike(Integer id);
    void removeLike(Integer id);
    void updateCompleted(Integer id);
    PostResultResponse getAllPosts(Pageable pageable);
    EachPostResponse getEachPost(Integer id);
    List<OtherPostResponse> getOtherPosts();
    PostResultResponse searchPosts(String keyword, Pageable pageable);
}
