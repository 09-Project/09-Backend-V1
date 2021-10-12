package com.example.project09.controller;

import com.example.project09.error.ErrorResponse;
import com.example.project09.payload.post.request.PostRequest;
import com.example.project09.payload.post.response.EachPostResponse;
import com.example.project09.payload.post.response.OtherPostResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 올리기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "S3와의 연결이 실패되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "상품 올리기", description = "제목, 내용, 가격, 거래 지역, 오픈 채팅방 링크, 상품 이미지, 상품 목적을 올린다.")
    public void createPost(@ModelAttribute @Valid PostRequest request) throws IOException {
        postService.createPost(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{post-id}", consumes = {"multipart/form-data"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품 수정하기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1.상품이 존재하지 않습니다.\t\n2.이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "S3와의 연결이 실패되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "상품 수정하기",
            description = "원하는 상품의 제목, 내용, 가격, 거래 지역, 오픈 채팅방 링크, 상품 이미지, 상품 목적을 수정한다.")
    public void modifyPost(@Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id,
                           @ModelAttribute @Valid PostRequest request) throws IOException {
        postService.modifyPost(request, id);
    }

    @DeleteMapping("/{post-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품 삭제하기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1.상품이 존재하지 않습니다.\t\n2.이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "S3와의 연결이 실패되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "상품 삭제하기", description = "원하는 상품을 삭제한다.")
    public void removePost(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.removePost(id);
    }

    @PutMapping("/{post-id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "종료 여부 변경 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "종료 여부 변경하기", description = "해당 상품이 거래 종료되었다면 Completed로 변경한다.")
    public void updateCompleted (
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.updateCompleted(id);
    }

    @GetMapping
    @PageableAsQueryParam
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 상품 불러오기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "전체 상품 보기", description = "전체 상품을 한 페이지에 16개씩 최신순으로 정렬해 조회한다.")
    public List<PostResponse> getAllPosts(
            @Parameter(hidden = true) @PageableDefault(size = 16, sort = "updatedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{post-id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 불러오기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404",
                    description = "1.상품이 존재하지 않습니다.\t\n2.이미지가 존재하지 않습니다.\t\n3.회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "상품 보기", description = "원하는 상품을 조회한다.")
    public EachPostResponse getEachPost(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        return postService.getEachPost(id);
    }

    @GetMapping("/other")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 불러오기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404",
                    description = "1.이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "다른 추천 상품 보기", description = "다른 8개의 상품을 최신순으로 정렬해 조회한다.")
    public List<OtherPostResponse> getOtherPosts() {
        return postService.getOtherPosts();
    }

    @PageableAsQueryParam
    @GetMapping("/search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과 불러오기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404",
                    description = "이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "상품 검색하기", description = "키워드가 제목에 포함된 상품을 한 페이지에 16개씩 최신순으로 정렬해 조회한다.")
    public List<PostResponse> searchPosts(@RequestParam String keyword,
            @Parameter(hidden = true) @PageableDefault(size = 16, sort = "updatedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.searchPosts(keyword, pageable);
    }

}
