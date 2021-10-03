package com.example.project09.controller;

import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.OtherPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "상품 API")
public class PostController {
    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "상품 올리기", description = "제목, 내용, 가격, 거래 지역, 오픈 채팅방 링크, 상품 이미지, 상품 목적을 올린다.")
    public void createPost(@Valid @ModelAttribute PostRequest request) throws IOException {
        postService.createPost(request);
    }

    @GetMapping
    @Operation(summary = "전체 상품 보기", description = "전체 상품을 한 페이지에 16개씩 최신순으로 정렬해 조회한다.")
    public List<PostResponse> getAllPosts(@PageableDefault(size = 16) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{post-id}")
    @Operation(summary = "상품 보기", description = "원하는 상품을 조회한다.")
    public EachPostResponse getEachPost(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        return postService.getEachPost(id);
    }

    @GetMapping("/other")
    @Operation(summary = "다른 추천 상품 보기", description = "다른 8개의 상품을 최신순으로 정렬해 조회한다.")
    public List<OtherPostResponse> getOtherPosts() {
        return postService.getOtherPosts();
    }

    @PatchMapping(path = "/{post-id}", consumes = {"multipart/form-data"})
    @Operation(summary = "상품 수정하기",
            description = "원하는 상품의 제목, 내용, 가격, 거래 지역, 오픈 채팅방 링크, 상품 이미지, 상품 목적을 수정한다.")
    public void modifyPost(@Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id,
                           @Valid @ModelAttribute PostRequest request) throws IOException {
        postService.modifyPost(request, id);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "상품 삭제하기", description = "원하는 상품을 삭제한다.")
    public void removePost(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.removePost(id);
    }

    @PostMapping("/like/{post-id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "찜하기", description = "원하는 상품에 찜을 추가한다.")
    public void addLike(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.addLike(id);
    }

    @DeleteMapping("/like/{post-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "찜 취소하기", description = "원하는 상품의 찜을 취소한다.")
    public void removeLike(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.removeLike(id);
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색하기", description = "키워드가 제목에 포함된 상품을 한 페이지에 16개씩 최신순으로 정렬해 조회한다.")
    public List<PostResponse> searchPosts(@RequestParam String keyword,
                                          @PageableDefault(size = 16) Pageable pageable) {
        return postService.searchPosts(keyword, pageable);
    }

}
