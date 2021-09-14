package com.example.project09.entity.member;

import com.example.project09.entity.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_member")
@JsonIgnoreProperties({"username", "password", "role", "posts"})
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
    private Integer everyLikeCounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts;

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
