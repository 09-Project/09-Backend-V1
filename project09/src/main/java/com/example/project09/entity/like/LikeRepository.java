package com.example.project09.entity.like;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByMemberIdAndPostId(Integer memberId, Integer postId);
    List<Like> findByMemberId(Integer id);
}
