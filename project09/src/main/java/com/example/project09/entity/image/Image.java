package com.example.project09.entity.image;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;
    private String originalFilename;
    private String filename;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "image")
    private Member member;
}
