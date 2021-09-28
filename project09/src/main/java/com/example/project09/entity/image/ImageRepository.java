package com.example.project09.entity.image;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByPostId(Integer id);
    Optional<Image> findByImagePath(String imagePath);
}
