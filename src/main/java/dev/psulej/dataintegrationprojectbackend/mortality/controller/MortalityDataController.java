package dev.psulej.dataintegrationprojectbackend.mortality.controller;

import dev.psulej.dataintegrationprojectbackend.mortality.convert.MortalityDataJsonImporter;
import dev.psulej.dataintegrationprojectbackend.mortality.convert.MortalityDataXmlImporter;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.convert.MortalityDataJsonExporter;
import dev.psulej.dataintegrationprojectbackend.mortality.convert.MortalityDataXmlExporter;
import dev.psulej.dataintegrationprojectbackend.mortality.service.MortalityDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/data/mortality")
@AllArgsConstructor
public class MortalityDataController {

    private final MortalityDataJsonImporter mortalityDataJsonImporter;
    private final MortalityDataService mortalityDataService;
    private final MortalityDataJsonExporter mortalityDataJsonExporter;
    private final MortalityDataXmlExporter mortalityDataXmlExporter;
    private final MortalityDataXmlImporter mortalityDataXmlImporter;

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

    @PostMapping("/import/json")
    public ResponseEntity<String> importJsonMortalityData(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Import File {}", file.getOriginalFilename());
            InputStream fileStream = file.getInputStream();
            mortalityDataJsonImporter.importData(fileStream);
            return new ResponseEntity<>("JSON import successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/import/xml")
    public ResponseEntity<String> importXmlMortalityData(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Import File {}", file.getOriginalFilename());
            InputStream fileStream = file.getInputStream();
            mortalityDataXmlImporter.importData(fileStream);
            return new ResponseEntity<>("XML import successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
