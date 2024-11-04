package com.example.sectors_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorDto {
    private Long id;
    private String valueTag;
    private String name;
    private List<SectorDto> children;
}

