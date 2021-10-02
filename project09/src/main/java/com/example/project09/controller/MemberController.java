package com.example.project09.controller;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
    }

    @PostMapping("/auth/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request);
    }

    @PutMapping("/auth/reissue")
    public TokenResponse reissue(@RequestHeader(name = "x-refresh-token") String token) {
        return memberService.reissue(token);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        memberService.updatePassword(request);
    }

    @PatchMapping(path = "/information", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void updateInfo(@Valid @ModelAttribute UpdateInformationRequest request) {
        memberService.updateInfo(request);
    }

    @GetMapping("/profile/{user-id}")
    public MemberProfileResponse getMemberProfile(@PathVariable(name = "user-id") Integer id) {
        return memberService.getMemberProfile(id);
    }

    @GetMapping("/me")
    public MemberMyPageResponse getMyPage() {
        return memberService.getMyPage();
    }

}
