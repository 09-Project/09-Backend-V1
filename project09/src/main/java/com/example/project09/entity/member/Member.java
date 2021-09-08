package com.example.project09.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, unique = true)
    private String name;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(length = 200)
    private String introduction;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String profileUrl;

    public Member updatePassword(String password) {
        this.password = password;
        return this;
    }

    public Member updateInfo(String name, String introduction) {
        this.name = name;
        this.introduction = introduction;
        return this;
    }

}
