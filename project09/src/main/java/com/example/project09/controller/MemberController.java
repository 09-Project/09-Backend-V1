package com.example.project09.controller;

import com.example.project09.error.ErrorResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.service.member.MemberService;
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
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "member", description = "사용자 관련 API")
public class MemberController {
    private final MemberService memberService;

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비밀번호 변경 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.기존 비밀번호가 일치하지 않습니다.\t\n2.Access 토큰이 유효하지 않습니다.\t\n3.Access 토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "비밀번호 변경", description = "기존 비밀번호와 새로운 비밀번호를 입력해서 변경한다.")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        memberService.updatePassword(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping(path = "/information", consumes = {"multipart/form-data"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 변경 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400",
                    description = "1.닉네임은 최대 10자까지 가능합니다.\t\n2.Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "해당 닉네임이 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "S3와의 연결이 실패되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원 정보 변경", description = "닉네임, 프로필 사진, 자기소개를 입력해 회원 정보를 수정한다.")
    public void updateInfo(@ModelAttribute @Valid UpdateInformationRequest request) {
        memberService.updateInfo(request);
    }

    @GetMapping("/{member-id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = MemberProfileResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 보기", description = "원하는 회원의 프로필 정보를 불러온다.")
    public MemberProfileResponse getMemberProfile(
            @Parameter(description = "member의 id") @PathVariable(name = "member-id") Integer id) {
        return memberService.getMemberProfile(id);
    }

    @GetMapping("/my-page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = MemberProfileResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "마이페이지 보기", description = "마이페이지의 프로필 정보를 불러온다.")
    public MemberProfileResponse getMyPage() {
        return memberService.getMyPage();
    }

    @GetMapping("/in-progress/{member-id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜한 게시글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "진행중인 게시글 보기", description = "해당 회원의 진행중인 게시글의 목록을 보여준다.")
    public List<PostResponse> getMemberInProgressPosts(
            @Parameter(description = "member의 id") @PathVariable(name = "member-id") Integer id) {
        return memberService.getMemberInProgressPosts(id);
    }

    @GetMapping("/completed/{member-id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "종료된 게시글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "종료된 게시글 보기", description = "해당 회원의 종료된 게시글의 목록을 보여준다.")
    public List<PostResponse> getMemberCompletedPosts(
            @Parameter(description = "member의 id") @PathVariable(name = "member-id") Integer id) {
        return memberService.getMemberCompletedPosts(id);
    }

    @GetMapping("/like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜한 게시글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "찜한 게시글 보기", description = "자신의 찜한 게시글의 목록을 보여준다.")
    public List<PostResponse> getMemberLikePosts() {
        return memberService.getMemberLikePosts();
    }

    @GetMapping("/information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = MemberInfoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "1.닉네임은 최대 10자까지 가능합니다.\t\n2.Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원 정보 불러오기", description = "자신의 닉네임, 프로필 사진, 자기소개를 불러온다.")
    public MemberInfoResponse getMyInfo() {
        return memberService.getMyInfo();
    }

}
