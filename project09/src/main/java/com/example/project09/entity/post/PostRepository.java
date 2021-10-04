package com.example.project09.entity.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
    List<Post> findByMemberId(Integer id);
    List<Post> findByTitleContaining(String keyword, Pageable pageable);
    Integer countByMemberId(Integer id);
    Integer countByMemberIdAndCompleted(Integer id, Completed completed);

    @Query(value = "select * from tbl_post order by id desc limit 8", nativeQuery = true)
    List<Post> getOtherPosts();
}
