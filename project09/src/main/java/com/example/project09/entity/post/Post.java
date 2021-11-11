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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tbl_post")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String transactionRegion;

    @Column(nullable = false)
    private String openChatLink;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Enumerated(EnumType.STRING)
    private Completed completed;
    private Integer likeCounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Like> likes = new HashSet<>();

    @OneToOne(mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private Image image;

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

    public Post addPostLikeCounts() {
        this.likeCounts++;
        return this;
    }

    public Post removeLikeCounts() {
        this.likeCounts--;
        return this;
    }

    public Post updateCompleted(Completed completed) {
        this.completed = completed;
        return this;
    }

}
