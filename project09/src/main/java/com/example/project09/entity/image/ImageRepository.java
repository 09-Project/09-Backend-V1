package com.example.project09.entity.image;

import com.example.project09.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByPostId(Integer id);
    List<Image> findByOrderByPostId(Post post);
}
