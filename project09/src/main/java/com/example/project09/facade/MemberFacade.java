package com.example.project09.facade;


import com.example.project09.security.auth.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberFacade {

    public static Integer getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ((CustomUserDetails)authentication.getPrincipal()).getMember().getId();
    }

}
