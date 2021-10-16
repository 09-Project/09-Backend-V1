package com.example.project09.controller;

import com.example.project09.error.ErrorResponse;
import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/auth")
@Tag(name = "auth", description = "사용자 인증 관련 API")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/signup")
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

    @PostMapping("/login")
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

    @PutMapping("/reissue")
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

}
