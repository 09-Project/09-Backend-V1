package com.example.project09.entity.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
    List<Post> findByMemberId(Integer id);
    List<Post> findByTitleContaining(String keyword, Pageable pageable);
}
