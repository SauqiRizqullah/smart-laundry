package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query(value = "SELECT * FROM m_images WHERE image_name = :imageName", nativeQuery = true)
    Optional<Image> searchName(@Param("imageName") String imageName);
}
