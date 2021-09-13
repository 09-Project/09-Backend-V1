package com.example.project09.entity.like;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findByMemberId(Integer id);
}
