package com.example.project09.controller;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostServiceImpl postService;

    @PostMapping(path = "/create", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@Valid @ModelAttribute PostRequest request) throws IOException {
        postService.createPost(request);
    }

    @PostMapping("/like/{post-id}")
    public void addLike(@PathVariable(name = "post-id") Integer id) {
        postService.addLike(id);
    }

    @DeleteMapping("/like/{post-id}")
    public void removeLike(@PathVariable(name = "post-id") Integer id) {
        postService.removeLike(id);
    }

    @DeleteMapping("/likes")
    public void removeAllLikes() {
        postService.removeAllLikes();
    }

    @PatchMapping(path = "/modify/{user-id}", consumes = {"multipart/form-data"})
    public void modifyPost(@PathVariable(name = "user-id") Integer id,
                           @Valid @ModelAttribute PostRequest request) throws IOException {
        postService.modifyPost(request, id);
    }

    @GetMapping("")
    public List<PostResponse> getAllPosts(@PageableDefault(size = 16) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{post-id}")
    public EachPostResponse getEachPost(@PathVariable(name = "post-id") Integer id) {
        return postService.getEachPost(id);
    }

    @GetMapping("/search")
    public List<PostResponse> searchPosts(@RequestParam String keyword,
                                          @PageableDefault(size = 16) Pageable pageable) {
        return postService.searchPosts(keyword, pageable);
    }

}
