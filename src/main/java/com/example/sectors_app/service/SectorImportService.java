package com.example.sectors_app.service;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.repository.SectorRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class SectorImportService {

    private final SectorRepository sectorRepository;

    public SectorImportService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    //TODO add logging

    @Transactional
    public void importSectors(String filePath) {
        try {
            File inputFile = new File(filePath);
            Document document = Jsoup.parse(inputFile, "UTF-8");

            List<Sector> existingSectors = sectorRepository.findAll();
            Map<String, Sector> existingSectorsMap = existingSectors.stream()
                    .collect(Collectors.toMap(Sector::getValueTag, sector -> sector));

            Map<Integer, Sector> levelMap = new HashMap<>();

            Elements selectElements = document.select("select[name=sectors]");
            if (!selectElements.isEmpty()) {
                Element selectElement = selectElements.first();
                Elements options = selectElement.select("option");

                for (Element option : options) {
                    String value = option.attr("value").trim();
                    String name = option.text().trim();

                    if (!value.isEmpty() && !name.isEmpty()) {
                        int level = countLeadingSpaces(option);
                        Sector parent = levelMap.get(level - 1);

                        Sector sector;
                        if (existingSectorsMap.containsKey(value)) {
                            sector = existingSectorsMap.get(value);
                            if (parent != null && !Objects.equals(sector.getParent(), parent)) {
                                sector.setParent(parent);
                                sectorRepository.save(sector);
                            }
                        } else {
                            sector = new Sector();
                            sector.setValueTag(value);
                            sector.setName(name);

                            if (parent != null) {
                                sector.setParent(parent);
                            }

                            sector = sectorRepository.save(sector);
                        }

                        levelMap.put(level, sector);
                        existingSectorsMap.put(value, sector);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading sectors from index.html", e);
        }
    }


    private int countLeadingSpaces(Element option) {
        String html = option.html();
        int spaceCount = 0;

        while (html.startsWith("&nbsp;")) {
            spaceCount++;
            html = html.substring(6);
        }
        return spaceCount / 4;
    }
}
