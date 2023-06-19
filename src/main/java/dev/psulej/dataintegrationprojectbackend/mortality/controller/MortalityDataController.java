package dev.psulej.dataintegrationprojectbackend.mortality.controller;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.export.MortalityDataJsonExporter;
import dev.psulej.dataintegrationprojectbackend.mortality.export.MortalityDataXmlExporter;
import dev.psulej.dataintegrationprojectbackend.mortality.service.MortalityDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/data/mortality")
@AllArgsConstructor
public class MortalityDataController {

    private final MortalityDataService mortalityDataService;
    private final MortalityDataJsonExporter mortalityDataJsonExporter;
    private final MortalityDataXmlExporter mortalityDataXmlExporter;

    @GetMapping
    public Page<MortalityData> getMortalityData(Pageable pageable) {
        return mortalityDataService.getMortalityData(pageable);
    }

    @GetMapping("/export/xml")
    public void exportXmlMortalityData(HttpServletResponse response) {
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mortality-data.xml");
            response.setContentType(MediaType.TEXT_HTML.toString());
            mortalityDataXmlExporter.export(response.getOutputStream());
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/export/json")
    public void exportJsonMoralityData(HttpServletResponse response) {
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mortality-data.json");
            response.setContentType(MediaType.TEXT_HTML.toString());
            mortalityDataJsonExporter.export(response.getOutputStream());
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
