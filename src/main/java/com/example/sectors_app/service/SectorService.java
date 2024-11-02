package com.example.sectors_app.service;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.repository.SectorRepository;

import java.util.List;

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

    public Sector getSectorById(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sector not fuond"));
    }
}
