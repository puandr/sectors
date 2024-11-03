package com.example.sectors_app.service;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.repository.SectorRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public List<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }

    public Sector saveSector(Sector sector) {
        return sectorRepository.save(sector);
    }

    public Optional<Sector> getSectorById(Long id) {

        Sector sector = sectorRepository.findById(id).orElse(null);
        if (sector != null) {
            Hibernate.initialize(sector.getChildren()); // Force initialization
        }
        return Optional.ofNullable(sector);
    }
}
