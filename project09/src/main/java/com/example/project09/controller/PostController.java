package com.example.project09.controller;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.security.auth.CustomUserDetails;
import com.example.project09.service.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostServiceImpl postService;

    @PostMapping(path = "/create", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@Valid @ModelAttribute PostRequest request,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.createPost(request, userDetails.getMember());
    }

    @PostMapping("/like/{post-id}")
    public void addLike(@PathVariable(name = "post-id") Integer id,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.addLike(id, userDetails.getMember());
    }

    @DeleteMapping("/like/{post-id}")
    public void removeLike(@PathVariable(name = "post-id") Integer id,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.removeLike(id, userDetails.getMember());
    }

    @PatchMapping(path = "/modify/{user-id}", consumes = {"multipart/form-data"})
    public void modifyPost(@PathVariable(name = "user-id") Integer id,
                           @ModelAttribute PostRequest request) {
        postService.modifyPost(request, id);
    }

    @GetMapping("")
    public List<PostResponse> getAllPosts(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{post-id}")
    public EachPostResponse getEachPost(@PathVariable(name = "post-id") Integer id) {
        return postService.getEachPost(id);
    }

    @GetMapping("/search")
    public List<PostResponse> searchPosts(@RequestParam String keyword,
                                          @PageableDefault(size = 16, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.searchPosts(keyword, pageable);
    }

}
