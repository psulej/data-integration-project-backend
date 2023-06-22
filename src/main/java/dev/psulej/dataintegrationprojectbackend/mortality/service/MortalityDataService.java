package dev.psulej.dataintegrationprojectbackend.mortality.service;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MortalityDataService {

    private final MortalityDataRepository mortalityDataRepository;

    public Page<MortalityData> getMortalityData(Pageable pageable) {
        return mortalityDataRepository.findAll(pageable);
    }

    @Transactional
    public void deleteMortalityData() {
        mortalityDataRepository.truncateTable();
    }
}