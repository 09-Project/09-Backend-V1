package com.example.project09.controller;

import com.example.project09.payload.member.request.InformationRequest;
import com.example.project09.payload.member.request.PasswordRequest;
import com.example.project09.payload.post.response.PostResponse;
import com.example.project09.security.auth.CustomUserDetails;
import com.example.project09.service.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PatchMapping(path = "/information", consumes = {"multipart/form-data"})
    public void updateInfo(@ModelAttribute InformationRequest request,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updateInfo(request, userDetails.getMember());
    }

    @GetMapping("/posts")
    public List<PostResponse> getMemberPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return memberService.getMemberPosts(userDetails.getMember());
    }

}
