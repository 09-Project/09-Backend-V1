package com.example.project09.facade;


import com.example.project09.entity.member.Member;
import com.example.project09.exception.UserNotFoundException;
import com.example.project09.security.auth.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberFacade {

    public static Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getPrincipal() == null )
            throw new UserNotFoundException();

        return ((CustomUserDetails)authentication).getMember();
    }

    public static Integer getMemberId() {
        return getMember().getId();
    }

}