package com.example.project09.entity.image;

import com.example.project09.entity.BaseTimeEntity;
import com.example.project09.entity.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_image")
@JsonIgnoreProperties("post")
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image updateImages(String images) {
        this.images = images;
        return this;
    }

}
