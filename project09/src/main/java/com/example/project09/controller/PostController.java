package com.example.project09.controller;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.security.auth.CustomUserDetails;
import com.example.project09.service.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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

    @PatchMapping(value = "/modify/{user-id}", consumes = {"multipart/form-data"})
    public void modifyPost(@PathVariable(name = "user-id") Integer id,
                           @ModelAttribute PostRequest request) {
        postService.modifyPost(request, id);
    }

}
