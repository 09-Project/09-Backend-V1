package com.example.project09.controller;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "사용자 회원가입", notes = "닉네임(10자 이내), 아이디, 비밀번호를 이용해 회원가입한다.")
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
    }

    @ApiOperation(value = "사용자 로그인", notes = "아이디, 비밀번호를 이용해 로그인한다.")
    @PostMapping("/auth/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request);
    }

    @ApiOperation(value = "토큰 재발급", notes = "Refresh 토큰을 이용해 새로운 Access 토큰을 발급받는다.")
    @PutMapping("/auth/reissue")
    public TokenResponse reissue(@RequestHeader(name = "x-refresh-token") String token) {
        return memberService.reissue(token);
    }

    @ApiOperation(value = "비밀번호 변경", notes = "기존 비밀번호와 새 비밀번호를 입력해 비밀번호를 변경한다.")
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        memberService.updatePassword(request);
    }

    @ApiOperation(value = "회원 정보 변경", notes = "닉네임, 자기소개, 프로필 사진을 변경한다.")
    @PatchMapping(path = "/information", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void updateInfo(@Valid @ModelAttribute UpdateInformationRequest request) {
        memberService.updateInfo(request);
    }

    @ApiOperation(value = "회원 정보 보기", notes = "닉네임, 자기소개, 프로필 사진을 불러온다.")
    @GetMapping("/information")
    public MemberInfoResponse getMyInfo() {
        return memberService.getMyInfo();
    }

    @ApiOperation(value = "프로필 보기", notes = "원하는 사용자의 회원 정보, 상품, 거래내역을 확인한다.")
    @GetMapping("/{member-id}")
    public MemberProfileResponse getMemberProfile(@PathVariable(name = "member-id") Integer id) {
        return memberService.getMemberProfile(id);
    }

    @ApiOperation(value = "마이페이지", notes = "자신의 회원 정보, 상품, 찜한 상품, 거래내역을 확인한다.")
    @GetMapping("/me")
    public MemberMyPageResponse getMyPage() {
        return memberService.getMyPage();
    }

}
