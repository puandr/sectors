package com.example.sectors_app.controller;

import com.example.sectors_app.dto.SectorDto;
import com.example.sectors_app.model.Sector;
import com.example.sectors_app.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sectors")
public class SectorController {

    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    @Operation(summary = "Get all sectors", description = "Retrieve a list of all sectors from the database, including parent-child relationships.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of sectors"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
//    public List<Sector> getAllSectors() {
//        return sectorService.getAllSectors();
//    }
    public List<SectorDto> getAllSectors() {
        return sectorService.getAllSectors().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sector by ID", description = "Retrieve a sector by its ID, including its children if any.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the sector"),
            @ApiResponse(responseCode = "404", description = "Sector not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id) {
        Sector sector = sectorService.getSectorById(id).orElse(null);
        if (sector != null) {
            sector.getChildren().size(); // Force initialization of children
        }
        return sector != null ? ResponseEntity.ok(sector) : ResponseEntity.notFound().build();
    }

    private SectorDto convertToDto(Sector sector) {
        SectorDto dto = new SectorDto();
        dto.setId(sector.getId());
        dto.setValueTag(sector.getValueTag());
        dto.setName(sector.getName());
        if (sector.getChildren() != null) {
            dto.setChildren(sector.getChildren().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

}
