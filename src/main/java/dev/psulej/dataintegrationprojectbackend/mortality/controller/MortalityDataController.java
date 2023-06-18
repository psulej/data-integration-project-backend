package dev.psulej.dataintegrationprojectbackend.mortality.controller;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.service.MortalityDataService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/mortality")
@AllArgsConstructor
public class MortalityDataController {

    private final MortalityDataService mortalityDataService;

    @GetMapping
    public Page<MortalityData> getMortalityData(Pageable pageable) {
        return mortalityDataService.getMortalityData(pageable);
    }
}
