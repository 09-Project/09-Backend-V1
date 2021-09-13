package com.example.project09.entity.like;

import com.example.project09.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByMemberAndPostId(Member member, Integer postId);
    Integer countByPostId(Integer id);
    List<Like> findByMemberId(Integer id);
}
