package com.example.project09.facade;

import com.example.project09.entity.member.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberFacade {

    public static Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof Member))
            return null;

        return (Member) authentication.getPrincipal();
    }

    public static Integer getMemberId() {
        if(getMember() == null) {
            return null;
        }

        return getMember().getId();
    }

    public static String getMemberName() {
        if(getMember() == null) {
            return null;
        }

        return getMember().getName();
    }

    public static String getMemberProfileUrl() {
        if(getMember() == null) {
            return null;
        }

        return getMember().getProfileUrl();
    }

}
