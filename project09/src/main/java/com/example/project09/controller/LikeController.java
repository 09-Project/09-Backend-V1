package com.example.project09.controller;

import com.example.project09.error.ErrorResponse;
import com.example.project09.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@Tag(name = "Like",description = "찜 API")
public class LikeController {
    private final PostService postService;

    @PostMapping("/{post-id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "찜하기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1.상품이 존재하지 않습니다.\t\n2.회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "찜이 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "찜하기", description = "원하는 상품에 찜을 추가한다.")
    public void addLike(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.addLike(id);
    }

    @DeleteMapping("/{post-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "찜 취소하기 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1.찜이 존재하지 않습니다.\t\n2.상품이 존재하지 않습니다.\t\n3.회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "찜 취소하기", description = "원하는 상품의 찜을 취소한다.")
    public void removeLike(
            @Parameter(description = "post의 id") @PathVariable(name = "post-id") Integer id) {
        postService.removeLike(id);
    }

}
