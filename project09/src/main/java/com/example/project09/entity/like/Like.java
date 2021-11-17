package com.example.project09.entity.like;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.post.Post;
import lombok.*;
import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tbl_like")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="like_uk",
                        columnNames = {"post_id", "member_id"}
                )
        }
)
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
