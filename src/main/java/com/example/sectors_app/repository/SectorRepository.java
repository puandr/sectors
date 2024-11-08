package com.example.sectors_app.repository;

import com.example.sectors_app.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    boolean existsByValueTag(String valueTag);

    @Query("SELECT s.valueTag FROM Sector s")
    List<String> findAllValueTags();

    Optional<Sector> findByValueTag(String valueTag);

    Sector findByName(String name);

    //
    @Query("SELECT s FROM Sector s LEFT JOIN FETCH s.children")
    List<Sector> findAllWithChildren();
}
