package com.example.project09.controller;

import com.example.project09.error.ErrorResponse;
import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member", description = "사용자 API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "닉네임은 최대 10자까지 가능합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "닉네임 또는 아이디가 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원 가입", description = "닉네임, 아이디, 비밀번호를 입력해서 가입한다.")
    public void signup(@RequestBody @Valid SignupRequest request) {
        memberService.signup(request);
    }

    @PostMapping("/auth/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "로그인", description = "아이디, 비밀번호를 입력해서 로그인한다.")
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return memberService.login(request);
    }

    @PutMapping("/auth/reissue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Refresh 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Refresh 토큰이 유효하지 않습니다.\t\n2.Refresh 토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Refresh 토큰이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "토큰 재발급", description = "Refresh 토큰을 이용해 새로운 Access 토큰을 발급받는다.")
    public TokenResponse reissue(@RequestHeader(name = "x-refresh-token") String token) {
        return memberService.reissue(token);
    }

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
            @ApiResponse(responseCode = "404",
                    description = "1.회원이 존재하지 않습니다.\t\n2.이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 보기", description = "원하는 회원의 프로필 정보를 불러온다.")
    public MemberProfileResponse getMemberProfile(
            @Parameter(description = "member의 id") @PathVariable(name = "member-id") Integer id) {
        return memberService.getMemberProfile(id);
    }

    @GetMapping("/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = MemberMyPageResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Access 토큰의 형태가 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1.Access 토큰이 만료되었습니다.\t\n2.Access 토큰이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1.회원이 존재하지 않습니다.\t\n2.이미지가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "마이페이지 보기", description = "자신의 프로필 정보와 찜 목록을 불러온다.")
    public MemberMyPageResponse getMyPage() {
        return memberService.getMyPage();
    }

}
