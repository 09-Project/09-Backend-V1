package com.example.project09.service.post;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

public interface PostService {
    void createPost(PostRequest request) throws IOException;
    void modifyPost(PostRequest request, Integer id) throws IOException;
    List<PostResponse> getAllPosts(Pageable pageable);
    EachPostResponse getEachPost(Integer id);
    void addLike(Integer id);
    void removeLike(Integer id);
    void removeAllLikes();
    List<PostResponse> searchPosts(String keyword, Pageable pageable);
}
