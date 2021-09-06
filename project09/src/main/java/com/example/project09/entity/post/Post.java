package com.example.project09.entity.post;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.like.Like;
import com.example.project09.entity.member.Member;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @NonNull
    @Max(value = 40)
    private String title;

    @Max(value = 500)
    private String content;

    @NonNull
    private Integer price;

    @NonNull
    private String transactionRegion;

    @NonNull
    private URL openChatLink;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private Set<Image> images = new HashSet<>();
}
