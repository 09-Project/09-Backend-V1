package com.example.project09.controller;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.security.auth.CustomUserDetails;
import com.example.project09.service.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberServiceImpl memberService;

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
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updatePassword(request, userDetails.getMember());
    }

    @PatchMapping(path = "/information", consumes = {"multipart/form-data"})
    public void updateInfo(@ModelAttribute UpdateInformationRequest request,
                           @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        memberService.updateInfo(request, userDetails.getMember());
    }

    @GetMapping("/profile/{user-id}")
    public MemberProfileResponse getMemberProfile(@PathVariable(name = "user-id") Integer id) {
        return memberService.getMemberProfile(id);
    }

    @GetMapping("/me")
    public MemberMyPageResponse getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return memberService.getMyPage(userDetails.getMember());
    }

}
