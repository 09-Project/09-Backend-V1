package com.example.project09.entity.post;

import com.example.project09.entity.BaseTimeEntity;
import com.example.project09.entity.image.Image;
import com.example.project09.entity.like.Like;
import com.example.project09.entity.member.Member;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(length = 40)
    private String title;

    @Column(length = 500)
    private String content;

    private Integer price;

    @NonNull
    private String transactionRegion;

    @NonNull
    private String openChatLink;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Image> images = new HashSet<>();

    public Post updatePurpose(Purpose purpose) {
        this.purpose = purpose;
        return this;
    }

    public Post updatePost(String title, String content, Integer price,
                           String transactionRegion, String openChatLink) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.transactionRegion = transactionRegion;
        this.openChatLink = openChatLink;
        return this;
    }

}
