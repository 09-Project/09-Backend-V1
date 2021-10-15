package com.example.project09.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tbl_member")
@JsonIgnoreProperties({"username", "password", "role"})
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 200)
    private String introduction;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String profileUrl;
    private Integer everyLikeCounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member updatePassword(String password) {
        this.password = password;
        return this;
    }

    public Member updateInfo(String name, String introduction, String profileUrl) {
        this.name = name;
        this.introduction = introduction;
        this.profileUrl = profileUrl;
        return this;
    }

    public Member addLikeCounts() {
        this.everyLikeCounts++;
        return this;
    }

    public Member removeLikeCounts() {
        this.everyLikeCounts--;
        return this;
    }

}

