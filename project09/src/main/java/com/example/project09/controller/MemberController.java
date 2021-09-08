package com.example.project09.controller;

import com.example.project09.payload.member.request.InformationRequest;
import com.example.project09.payload.member.request.PasswordRequest;
import com.example.project09.security.auth.CustomUserDetails;
import com.example.project09.service.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberServiceImpl memberService;

    @PatchMapping("/password")
    public void updatePassword(@RequestBody PasswordRequest request,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updatePassword(request, userDetails.getMember());
    }

    @PatchMapping("/information")
    public void updateInfo(@RequestBody InformationRequest request) {
        memberService.updateInfo(request);
    }
}
