package com.example.project09.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByNameOrUsername(String name, String username);
    Optional<Member> findByUsername(String username);
}
